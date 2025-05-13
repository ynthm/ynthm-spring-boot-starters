package com.ynthm.autoconfigure.cache.config;

import com.ynthm.autoconfigure.cache.support.CacheConst;
import com.ynthm.autoconfigure.cache.support.CacheMessageListener;
import com.ynthm.autoconfigure.cache.support.RedisCaffeineCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


/**
 * @author Ethan Wang
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheRedisCaffeineProperties.class)
public class CacheRedisCaffeineAutoConfiguration {

  private CacheRedisCaffeineProperties cacheRedisCaffeineProperties;

  @Autowired
  public void setCacheRedisCaffeineProperties(CacheRedisCaffeineProperties cacheRedisCaffeineProperties) {
    this.cacheRedisCaffeineProperties = cacheRedisCaffeineProperties;
  }

  @Bean(CacheConst.CACHE_MANAGER_L2)
  @ConditionalOnBean(RedisTemplate.class)
  public RedisCaffeineCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
    log.info("===========================================");
    log.info("=                                         =");
    log.info("=          Two Level Cache Start          =");
    log.info("=                                         =");
    log.info("===========================================");
    return new RedisCaffeineCacheManager(cacheRedisCaffeineProperties, redisTemplate);
  }

  @Bean
  @ConditionalOnBean(RedisTemplate.class)
  public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> stringRedisTemplate,
                                                                     RedisCaffeineCacheManager redisCaffeineCacheManager) {
    RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(stringRedisTemplate.getConnectionFactory());
    CacheMessageListener cacheMessageListener = new CacheMessageListener(stringRedisTemplate, redisCaffeineCacheManager);
    redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(cacheRedisCaffeineProperties.getRedis().getTopic()));
    return redisMessageListenerContainer;
  }
}
