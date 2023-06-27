package com.ynthm.common.util;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class BeanUtilTest {

  @Data
  static class User {
    private String name;
    private int age;
  }

  @Test
  void test() {
    User user = new User();
    BeanUtil.setProperty(User.class, user, "name", "Ethan Wang");
    BeanUtil.setPropertyIntroSpector(User.class, user, "age", 18);
    Assertions.assertEquals(
        "Ethan Wang", BeanUtil.getPropertyIntroSpector(User.class, user, "name"));
    Assertions.assertEquals(18, BeanUtil.getProperty(User.class, user, "age"));
  }
}
