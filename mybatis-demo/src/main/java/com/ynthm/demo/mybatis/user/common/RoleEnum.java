package com.ynthm.demo.mybatis.user.common;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 枚举属性，实现 IEnum 接口
 *
 * <p>mybatis-plus.typeEnumsPackage
 *
 * @author ethan
 */
public enum RoleEnum implements IEnum<Integer> {
  ADMIN(1, "ROLE_ADMIN"),
  AGENT(2, "ROLE_AGENT");

  private int value;
  private String name;

  RoleEnum(int value, String name) {
    this.value = value;
    this.name = name;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }

  public String getName() {
    return this.name;
  }
}
