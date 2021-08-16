package com.ynthm.common.util;

import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class BeanUtilTest {

  class User {
    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }

  @Test
  void test() throws Exception {
    User user = new User();
    BeanUtil.setProperty(User.class, user, "name", "Ethan Wang");
    BeanUtil.setPropertyIntroSpector(User.class, user, "age", 18);
    System.out.println(BeanUtil.getPropertyIntroSpector(User.class, user, "name"));
    System.out.println(BeanUtil.getProperty(User.class, user, "age"));
  }
}
