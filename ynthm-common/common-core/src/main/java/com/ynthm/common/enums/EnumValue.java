package com.ynthm.common.enums;

/**
 * 底层枚举接口 枚举值
 *
 * @param <T> 推荐使用 Integer 类型 当然支持 String
 * @author Ethan Wang
 */
public interface EnumValue<T> {
  T value();
}
