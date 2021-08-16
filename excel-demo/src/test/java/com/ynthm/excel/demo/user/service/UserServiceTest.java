package com.ynthm.excel.demo.user.service;

import cn.hutool.core.util.RandomUtil;
import com.ynthm.excel.demo.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {

  @Autowired private UserService userService;

  @Test
  void test() {
    long start = System.currentTimeMillis();
    List<User> userList = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      User user = new User();
      user.setName("name" + i);
      user.setAge(RandomUtil.randomInt(1, 160));
      user.setAmount(new BigDecimal(String.valueOf(RandomUtil.randomInt(100, 10000))));
      userList.add(user);
    }
    userService.saveBatchExecutorType(userList);
    long end = System.currentTimeMillis();
    System.out.println("---------------" + (start - end) + "---------------");
  }
}
