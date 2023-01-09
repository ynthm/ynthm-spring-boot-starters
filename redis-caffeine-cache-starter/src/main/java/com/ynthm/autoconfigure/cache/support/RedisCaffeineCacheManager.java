package com.ynthm.autoconfigure.cache.support;

import com.ynthm.autoconfigure.cache.config.CacheRedisCaffeineProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ethan Wang
 */
@Slf4j
public class RedisCaffeineCacheManager implements CacheManager {

  private static ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

  private final CacheRedisCaffeineProperties cacheRedisCaffeineProperties;

  private final RedisTemplate<Object, Object> stringKeyRedisTemplate;

  private final boolean dynamic;

  private Set<String> cacheNames;

  {
    cacheNames = new HashSet<>();
    cacheNames.add(CacheNames.CACHE_15_MIN);
    cacheNames.add(CacheNames.CACHE_30MIN);
    cacheNames.add(CacheNames.CACHE_60MIN);
    cacheNames.add(CacheNames.CACHE_2HOUR);
    cacheNames.add(CacheNames.CACHE_12HOUR);
  }

  public RedisCaffeineCacheManager(CacheRedisCaffeineProperties cacheRedisCaffeineProperties,
                                   RedisTemplate<Object, Object> stringKeyRedisTemplate) {
    super();
    this.cacheRedisCaffeineProperties = cacheRedisCaffeineProperties;
    this.stringKeyRedisTemplate = stringKeyRedisTemplate;
    this.dynamic = cacheRedisCaffeineProperties.isDynamic();
  }

  //——————————————————————— 进行缓存工具 ——————————————————————

  /**
   * 清除所有进程缓存
   */
  public void clearAllCache() {
    stringKeyRedisTemplate.convertAndSend(cacheRedisCaffeineProperties.getRedis().getTopic(), new CacheMessage(null, null));
  }

  /**
   * 返回所有进程缓存(二级缓存)的统计信息
   * result:{"缓存名称":统计信息}
   *
   * @return
   */
  public static Map<String, CacheStats> getCacheStats() {
    if (CollectionUtils.isEmpty(cacheMap)) {
      return null;
    }

    Map<String, CacheStats> result = new LinkedHashMap<>();
    for (Cache cache : cacheMap.values()) {
      RedisCaffeineCache caffeineCache = (RedisCaffeineCache) cache;
      result.put(caffeineCache.getName(), caffeineCache.getCaffeineCache().stats());
    }
    return result;
  }

  @Override
  public Cache getCache(String name) {
    Cache cache = cacheMap.get(name);
    if (cache != null) {
      return cache;
    }
    if (!dynamic && !cacheNames.contains(name)) {
      return null;
    }

    cache = new RedisCaffeineCache(name, stringKeyRedisTemplate, caffeineCache(name), cacheRedisCaffeineProperties);
    Cache oldCache = cacheMap.putIfAbsent(name, cache);
    log.debug("create cache instance, the cache name is : {}", name);
    return oldCache == null ? cache : oldCache;
  }

  @Override
  public Collection<String> getCacheNames() {
    return this.cacheNames;
  }

  public void clearLocal(String cacheName, Object key) {
    // cacheName为null 清除所有进程缓存
    if (cacheName == null) {
      log.info("清除所有本地缓存");
      cacheMap = new ConcurrentHashMap<>();
      return;
    }

    Cache cache = cacheMap.get(cacheName);
    if (cache == null) {
      return;
    }

    RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
    redisCaffeineCache.clearLocal(key);
  }

  /**
   * 实例化本地一级缓存
   *
   * @param name
   * @return
   */
  private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(String name) {
    Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
    CacheRedisCaffeineProperties.CacheDefault cacheConfig;
    switch (name) {
      case CacheNames.CACHE_15_MIN:
        cacheConfig = cacheRedisCaffeineProperties.getCache15m();
        break;
      case CacheNames.CACHE_30MIN:
        cacheConfig = cacheRedisCaffeineProperties.getCache30m();
        break;
      case CacheNames.CACHE_60MIN:
        cacheConfig = cacheRedisCaffeineProperties.getCache60m();
        break;
      case CacheNames.CACHE_2HOUR:
        cacheConfig = cacheRedisCaffeineProperties.getCache2h();
        break;
      case CacheNames.CACHE_12HOUR:
        cacheConfig = cacheRedisCaffeineProperties.getCache12h();
        break;
      default:
        cacheConfig = cacheRedisCaffeineProperties.getCacheDefault();
    }
    Duration refreshAfterWrite = cacheConfig.getRefreshAfterWrite();
    Duration expireAfterAccess = cacheConfig.getExpireAfterAccess();
    Duration expireAfterWrite = cacheConfig.getExpireAfterWrite();
    int initialCapacity = cacheConfig.getInitialCapacity();
    long maximumSize = cacheConfig.getMaximumSize();

    log.debug("本地缓存初始化：");
    if (!refreshAfterWrite.isZero()) {
      // 创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
      cacheBuilder.refreshAfterWrite(refreshAfterWrite);
    }
    if (!expireAfterAccess.isZero()) {
      log.debug("设置本地缓存访问后过期时间，{}秒", expireAfterAccess);
      cacheBuilder.expireAfterAccess(expireAfterAccess);
    }
    if (!expireAfterWrite.isZero()) {
      log.debug("设置本地缓存写入后过期时间，{}秒", expireAfterWrite);
      cacheBuilder.expireAfterWrite(expireAfterWrite);
    }
    if (initialCapacity > 0) {
      log.debug("设置缓存初始化大小{}", initialCapacity);
      cacheBuilder.initialCapacity(initialCapacity);
    }
    if (maximumSize > 0) {
      log.debug("设置本地缓存最大值{}", maximumSize);
      cacheBuilder.maximumSize(maximumSize);
    }

    cacheBuilder.recordStats();
    return cacheBuilder.build();
  }
}
