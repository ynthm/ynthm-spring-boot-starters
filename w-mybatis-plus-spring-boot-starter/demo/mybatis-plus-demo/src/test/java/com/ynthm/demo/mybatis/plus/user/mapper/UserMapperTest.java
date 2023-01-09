package com.ynthm.demo.mybatis.plus.user.mapper;

import com.ynthm.demo.mybatis.plus.enums.Gender;
import com.ynthm.demo.mybatis.plus.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author Ethan Wang
 */
class UserMapperTest extends BaseMapperTest {

  @Resource private UserMapper userMapper;

  @Test
  void testCrud() {
    User user =
        new User()
            .setUsername("Ethan Wang")
            .setPassword("123456")
            .setPhoneNumber("18877776666")
            .setGender(Gender.MALE);
    userMapper.insert(user);

    String username = "Ethan";
    user.setUsername(username);
    userMapper.updateById(user);
    Assertions.assertEquals(username, userMapper.selectById(user.getId()).getUsername());

    userMapper.deleteById(user);
    Assertions.assertNull(userMapper.selectById(user.getId()));
  }

  @Test
  void testAr() {
    User user =
        new User()
            .setUsername("Wang")
            .setPassword("123456")
            .setPhoneNumber("18877776666")
            .setGender(Gender.MALE);
    user.insert();
    Assertions.assertEquals(1, userMapper.deleteById(user));
  }
}
