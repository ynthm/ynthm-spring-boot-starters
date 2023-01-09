package com.ynthm.demo.mybatis.plus.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author Ethan Wang
 */
@Getter
public enum UserStatus {
  /**
   * 创建
   */
  CREATED(0),
  ACTIVATED(1),
  LOCKED(2),
  CLOSED(3),
  ;

  @JsonValue
  @EnumValue
  private final int value;

  UserStatus(int value) {
    this.value = value;
  }
}
