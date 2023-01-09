package com.ynthm.demo.cache.demo.controller;

import com.ynthm.autoconfigure.cache.support.CacheConst;
import com.ynthm.autoconfigure.cache.support.CacheNames;
import com.ynthm.demo.mybatis.plus.user.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ethan Wang
 */
@RestController
public class DemoController {

  @Cacheable(
      cacheNames = CacheNames.CACHE_15_MIN,
      keyGenerator = "myKeyGenerator",
      cacheManager = CacheConst.CACHE_MANAGER_L2)
  @GetMapping("/demo")
  public String demo() {
    return "hello world";
  }

  @Cacheable(value = CacheNames.CACHE_30MIN, key = "#id")
  @GetMapping("/user/{id}")
  public SampleVo getUserById(@PathVariable(value = "id") Integer id) {
    return new SampleVo("张三" + id);
  }

  @Cacheable(value = "user", key = "#p0")
  public User getUserById1(Long id) {
    return null;
  }

  @Cacheable(value = "user", key = "#userDTO.id")
  public User getUserById2(User userDTO) {
    return null;
  }

  @Cacheable(value = "user", key = "#p0.id")
  public User getUserById3(User userDTO) {
    return null;
  }
}
