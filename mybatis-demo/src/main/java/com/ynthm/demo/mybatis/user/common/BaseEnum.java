package com.ynthm.demo.mybatis.user.common;

/** @author ethan */
public interface BaseEnum<E extends BaseEnum<?>> {
  int getValue();
}
