package com.ynthm.autoconfigure.cache.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 缓存监听器 使用 Redis 消息队列删除过期 key
 *
 * @author Ethan Wang
 */
@Slf4j
public class CacheMessageListener implements MessageListener {
  private final RedisTemplate<Object, Object> redisTemplate;

  private final RedisCaffeineCacheManager redisCaffeineCacheManager;

  public CacheMessageListener(RedisTemplate<Object, Object> redisTemplate,
                              RedisCaffeineCacheManager redisCaffeineCacheManager) {
    this.redisTemplate = redisTemplate;
    this.redisCaffeineCacheManager = redisCaffeineCacheManager;
  }

  /**
   * 利用 redis 发布订阅通知其他节点清除本地缓存
   *
   * @param message
   * @param pattern
   */
  @Override
  public void onMessage(Message message, byte[] pattern) {
    CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
    log.debug("收到redis清除缓存消息, 开始清除本地缓存, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
    redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
  }
}
