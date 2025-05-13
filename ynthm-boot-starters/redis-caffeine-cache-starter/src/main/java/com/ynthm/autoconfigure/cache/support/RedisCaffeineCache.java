package com.ynthm.autoconfigure.cache.support;

import com.ynthm.autoconfigure.cache.config.CacheRedisCaffeineProperties;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ethan Wang
 */
@Slf4j
public class RedisCaffeineCache extends AbstractValueAdaptingCache {
  private String name;

  private RedisTemplate<Object, Object> redisTemplate;

  @Getter
  private Cache<Object, Object> caffeineCache;

  private String cachePrefix;

  /**
   * 默认 key 超时时间 3600s
   */
  private Duration defaultExpiration = Duration.ofHours(1);

  private static final Map<String, Duration> defaultExpires = new HashMap<>();

  static {
    defaultExpires.put(CacheNames.CACHE_15_MIN, Duration.ofMinutes(15));
    defaultExpires.put(CacheNames.CACHE_30MIN, Duration.ofMinutes(30));
    defaultExpires.put(CacheNames.CACHE_60MIN, Duration.ofMinutes(60));
    defaultExpires.put(CacheNames.CACHE_2HOUR, Duration.ofHours(2));
    defaultExpires.put(CacheNames.CACHE_12HOUR, Duration.ofHours(12));
  }

  private String topic;
  private Map<String, ReentrantLock> keyLockMap = new ConcurrentHashMap<>();

  protected RedisCaffeineCache(boolean allowNullValues) {
    super(allowNullValues);
  }

  public RedisCaffeineCache(String name, RedisTemplate<Object, Object> redisTemplate,
                            Cache<Object, Object> caffeineCache, CacheRedisCaffeineProperties cacheRedisCaffeineProperties) {
    super(cacheRedisCaffeineProperties.isCacheNullValues());
    this.name = name;
    this.redisTemplate = redisTemplate;
    this.caffeineCache = caffeineCache;
    this.cachePrefix = cacheRedisCaffeineProperties.getCachePrefix();
    this.defaultExpiration = cacheRedisCaffeineProperties.getRedis().getDefaultExpiration();
    this.topic = cacheRedisCaffeineProperties.getRedis().getTopic();
    defaultExpires.putAll(cacheRedisCaffeineProperties.getRedis().getExpires());
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getNativeCache() {
    return this;
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    Object value = lookup(key);
    if (value != null) {
      return (T) value;
    }
    //key在redis和缓存中均不存在
    ReentrantLock lock = keyLockMap.get(key.toString());

    if (lock == null) {
      log.debug("create lock for key : {}", key);
      keyLockMap.putIfAbsent(key.toString(), new ReentrantLock());
      lock = keyLockMap.get(key.toString());
    }
    lock.lock();
    try {
      value = lookup(key);
      if (value != null) {
        return (T) value;
      }
      //执行原方法获得value
      value = valueLoader.call();
      Object storeValue = toStoreValue(value);
      put(key, storeValue);
      return (T) value;
    } catch (Exception e) {
      throw new ValueRetrievalException(key, valueLoader, e.getCause());
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void put(Object key, Object value) {
    if (!super.isAllowNullValues() && value == null) {
      this.evict(key);
      return;
    }
    Duration expire = getExpire();
    log.debug("put：{},expire:{}", getKey(key), expire);
    redisTemplate.opsForValue().set(getKey(key), toStoreValue(value), expire);

    // 缓存变更时通知其他节点清理本地缓存
    push(new CacheMessage(this.name, key));
    // 此处put没有意义，会收到自己发送的缓存key失效消息
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    Object cacheKey = getKey(key);
    // 使用setIfAbsent原子性操作
    Duration expire = getExpire();
    boolean setSuccess;
    setSuccess = redisTemplate.opsForValue().setIfAbsent(getKey(key), toStoreValue(value), expire);

    Object hasValue;
    //setNx结果
    if (setSuccess) {
      push(new CacheMessage(this.name, key));
      hasValue = value;
    } else {
      hasValue = redisTemplate.opsForValue().get(cacheKey);
    }

    caffeineCache.put(key, toStoreValue(value));
    return toValueWrapper(hasValue);
  }

  @Override
  public void evict(Object key) {
    // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
    redisTemplate.delete(getKey(key));

    push(new CacheMessage(this.name, key));

    caffeineCache.invalidate(key);
  }

  @Override
  public void clear() {
    // 先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
    Set<Object> keys = redisTemplate.keys(this.name.concat(":*"));
    for (Object key : keys) {
      redisTemplate.delete(key);
    }

    push(new CacheMessage(this.name, null));
    caffeineCache.invalidateAll();
  }

  /**
   * 取值逻辑
   *
   * @param key
   * @return
   */
  @Override
  protected Object lookup(Object key) {
    Object cacheKey = getKey(key);
    Object value = caffeineCache.getIfPresent(key);
    if (value != null) {
      log.debug("从本地缓存中获得key, the key is : {}", cacheKey);
      return value;
    }

    value = redisTemplate.opsForValue().get(cacheKey);

    if (value != null) {
      log.debug("从redis中获得值，将值放到本地缓存中, the key is : {}", cacheKey);
      caffeineCache.put(key, value);
    }
    return value;
  }

  /**
   * @description 清理本地缓存
   */
  public void clearLocal(Object key) {
    log.debug("clear local cache, the key is : {}", key);
    if (key == null) {
      caffeineCache.invalidateAll();
    } else {
      caffeineCache.invalidate(key);
    }
  }

  //————————————————————————————私有方法——————————————————————————

  private Object getKey(Object key) {
    String keyStr = this.name.concat(":").concat(key.toString());
    return StringUtils.hasLength(this.cachePrefix) ? this.cachePrefix.concat(":").concat(keyStr) : keyStr;
  }

  private Duration getExpire() {
    return Optional.ofNullable(defaultExpires.get(this.name))
            .orElse(defaultExpiration);
  }

  /**
   * @description 缓存变更时通知其他节点清理本地缓存
   */
  private void push(CacheMessage message) {
    redisTemplate.convertAndSend(topic, message);
  }
}


