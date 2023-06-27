package com.ynthm.autoconfigure.cache.util;

import com.ynthm.autoconfigure.cache.config.CacheRedisCaffeineAutoConfiguration;
import com.ynthm.autoconfigure.cache.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Ethan Wang
 */
//@TestPropertySource("classpath:application.yaml")
//@ContextConfiguration("classpath:application.yaml")
@SpringBootTest(classes = {RedisConfig.class, RedisAutoConfiguration.class, CacheRedisCaffeineAutoConfiguration.class})
class RedisUtilTest {

  @Autowired private
  RedisUtil redisUtil;

  @Test
  void hEntries() {
    String key = "dict:";
    HashMap<String, Object> map = new HashMap<>();
    map.put("1", "1");
    map.put("2",2);
    redisUtil.hPutAll(key, map);
    Map<String, Object> objectObjectMap = redisUtil.hEntries(key);


    System.out.println(objectObjectMap);
  }
}