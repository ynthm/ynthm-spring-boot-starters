package com.ynthm.excel.demo.redis.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/** @author Ethan Wang */
@Component
public class RedisSlidingWindowLua {
  /** 调用 Lua 脚本请使用 StringRedisTemplate */
  @Autowired private StringRedisTemplate stringRedisTemplate;

  @Autowired private DefaultRedisScript<Boolean> slidingWindow;

  public boolean block(List<String> keys, int limit, int seconds) {
    Boolean block =
        stringRedisTemplate.execute(
            slidingWindow,
            keys,
            String.valueOf(limit),
            String.valueOf(System.currentTimeMillis()),
            UUID.randomUUID().toString(),
            String.valueOf(seconds));
    assert block != null;
    return block;
  }

  public boolean block1(List<String> keys, LimitAccess limitAccess) {
    Boolean block =
        stringRedisTemplate.execute(
            slidingWindow,
            keys,
            String.valueOf(limitAccess.capacity()),
            String.valueOf(System.currentTimeMillis()),
            UUID.randomUUID().toString(),
            String.valueOf(limitAccess.timeUnit().toSeconds(limitAccess.timeWindow())));
    assert block != null;
    return block;
  }
}
