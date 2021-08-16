package com.ynthm.excel.demo.web.controller;

import com.ynthm.excel.demo.redis.interceptor.AccessLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/** @author Ethan Wang */
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisDemoController {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private StringRedisTemplate stringRedisTemplate;

  /** Value 存入Long 获取 为 Integer 代码中用Long 类型接收报类型转化错误 */
  @Resource(name = "stringRedisTemplate")
  private ValueOperations<String, Object> valueOperations;

  @AccessLimit(limit = 1, seconds = 60)
  @GetMapping("test01")
  public Long get01() {
    ValueOperations<String, String> valueOperations1 = stringRedisTemplate.opsForValue();
    String key = "redis:demo:abc";
    valueOperations1.set(key, "123", 1L, TimeUnit.DAYS);

    BoundValueOperations<String, String> boundValueOperations =
        stringRedisTemplate.boundValueOps(key);
    System.out.println(boundValueOperations.get());

    String key1 = "redis:demo:limit";
    Object value = this.valueOperations.get(key1);
    if (value == null) {
      this.valueOperations.set(key1, 1L, 1L, TimeUnit.DAYS);
    }

    Long increment = this.valueOperations.increment(key1, 1L);

    System.out.println(increment);
    return increment;
  }
}
