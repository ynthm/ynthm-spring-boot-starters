package com.ynthm.excel.demo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpringBootTest
class RedisTemplateTest {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private StringRedisTemplate stringRedisTemplate;

  /** Value 存入Long 获取 为 Integer 代码中用Long 类型接收报类型转化错误 */
  @Resource(name = "stringRedisTemplate")
  private ValueOperations<String, Object> valueOperations;

  @Autowired private ValueOperations<String, Object> valueOps;

  @Autowired private DefaultRedisScript<Long> fixedWindow;

  @Test
  void test1() {
    String key = "redis:demo:abc";
    stringRedisTemplate.opsForValue().get(key);
  }

  @Test
  void test2() {
    URL resource =
        ResourceUtil.getResource(File.separator + "lua" + File.separator + "fixed-window.lua");
    String s = FileUtil.readString(resource, StandardCharsets.UTF_8.name());
    // spring-boot-starter-data-redis 返回类型不支持 Integer
    // org.springframework.data.redis.connection.ReturnType
    RedisScript<Long> redisScript = new DefaultRedisScript<>(s, Long.class);
    String keyLimit = "key:abc";
    ArrayList<String> keys = CollUtil.newArrayList(keyLimit);
    Long result = stringRedisTemplate.execute(redisScript, keys, "2", "60");
    Long result1 = stringRedisTemplate.execute(redisScript, keys, "2", "60");
    Long result2 = stringRedisTemplate.execute(redisScript, keys, "2", "60");
    System.out.println(stringRedisTemplate.boundValueOps(keyLimit).get());
  }

  @Test
  void test3() {

    String keyLimit = "key:001";
    ArrayList<String> keys = CollUtil.newArrayList(keyLimit);
    Long result = stringRedisTemplate.execute(fixedWindow, keys, "2", "60");
    Long result1 = stringRedisTemplate.execute(fixedWindow, keys, "2", "60");
    Long result2 = stringRedisTemplate.execute(fixedWindow, keys, "2", "60");
    System.out.println(stringRedisTemplate.boundValueOps(keyLimit).get());
  }

  @Test
  void test4() {}
}
