package com.ynthm.demo.mybatis.plus.user.enums;

import lombok.Getter;

/**
 * @author Ethan Wang
 */
@Getter
public enum PermissionType {
  /**
   * 菜单
   */
  MENU(0),
  PAGE(1),
  OPERATION(2),
  /**
   * 字段权限
   */
  DATA(3),
  ;

  private final int value;

  PermissionType(int value) {
    this.value = value;
  }
}
