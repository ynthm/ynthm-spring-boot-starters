package com.ynthm.excel.demo.user.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.ynthm.excel.demo.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@SpringBootTest
class UserMapperTest {

  @Autowired UserMapper userMapper;

  @Test
  void test() {
    User user = userMapper.selectByPrimaryKey(1);

    userMapper.selectByExample(
        new Example(User.class).createCriteria().andEqualTo("name", "Ethan " + "Wang"));
    Assertions.assertNotNull(user);
  }

  @Test
  void queryByName() {
    List<User> userList = userMapper.queryUserByName("Ethan Wang");

    System.out.println(userList.size());
  }

  @Test
  void selectByIds() {
    List<User> users = userMapper.selectUsersByIds(CollUtil.newArrayList(1, 5, 10));
    System.out.println(users.size());
  }

  @Test
  void saveBatch() {
    List<User> userList = new ArrayList<>();
    User user = new User();
    user.setName("Liu");
    user.setAge(19);
    userList.add(user);
    userMapper.batchInsert(userList);
  }

  @Test
  void testSingleInsertList() {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      User user = new User();
      user.setName("name" + i);
      user.setAge(RandomUtil.randomInt(1, 160));
      user.setAmount(new BigDecimal(String.valueOf(RandomUtil.randomInt(100, 10000))));
      userMapper.insert(user);
    }
    long end = System.currentTimeMillis();
    System.out.println("---------------" + (start - end) + "---------------");
  }

  @Test
  void test002() {
    User user = new User();
    user.setName("Ethan Wang");
    user.setAge(18);
    List<User> users = userMapper.queryByUser(user);
  }
}
