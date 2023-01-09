package com.ynthm.demo.mybatis.plus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.ynthm.common.web.enums.Enumerator;

/**
 * 性别
 *
 * @author Ethan Wang
 */
public enum Gender implements Enumerator {

  /** 未知 */
  UNKNOWN(-1),
  FEMALE(0),
  MALE(1),
  ;

  @EnumValue private final int value;

  Gender(int value) {
    this.value = value;
  }

  @Override
  public int value() {
    return value;
  }
}
