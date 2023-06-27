package com.ynthm.common.excel.util.data;

import com.ynthm.autoconfigure.excel.annotation.IntegerEnum;
import java.util.Arrays;
import lombok.AllArgsConstructor;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@AllArgsConstructor
public enum Gender4Integer implements IntegerEnum {
  /** 未知 */
  UNKNOWN(-1, "未知"),
  FEMALE(0, "女"),
  MALE(1, "男"),
  ;

  private final int value;

  private final String label;

  public static Gender4Integer getByValue(int value) {
    return Arrays.stream(Gender4Integer.values())
        .filter(i -> i.value() == value)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Integer value() {
    return value;
  }

  @Override
  public String label() {
    return label;
  }
}
