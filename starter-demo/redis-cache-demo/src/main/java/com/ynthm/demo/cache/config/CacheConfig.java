package com.ynthm.demo.cache.config;

import com.ynthm.autoconfigure.cache.support.CacheNames;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ethan Wang
 */
@Configuration
public class CacheConfig {

  @Primary
  @Bean("defaultCacheManager")
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {

    // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
    RedisCacheConfiguration defaultConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    RedisSerializer.string()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

    // 设置缓存的默认过期时间，也是使用Duration设置
    defaultConfig = defaultConfig.entryTtl(Duration.ofMinutes(2)).disableCachingNullValues();

    // 设置一个初始化的缓存空间set集合
    Set<String> cacheNames = new HashSet<>();
    cacheNames.add(CacheNames.CACHE_15_MIN);
    cacheNames.add(CacheNames.CACHE_30MIN);

    // 对每个缓存空间应用不同的配置
    Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
    configMap.put(
        CacheNames.CACHE_15_MIN,
        defaultConfig.prefixCacheNameWith("m15").entryTtl(Duration.ofMinutes(15)));
    configMap.put(
        CacheNames.CACHE_30MIN,
        defaultConfig.prefixCacheNameWith("m30").entryTtl(Duration.ofMinutes(30)));

    // 使用自定义的缓存配置初始化一个cacheManager 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
    RedisCacheManager cacheManager =
        RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
            .cacheDefaults(defaultConfig)
            .initialCacheNames(cacheNames)
            .withInitialCacheConfigurations(configMap)
            .build();
    return cacheManager;
  }
}
