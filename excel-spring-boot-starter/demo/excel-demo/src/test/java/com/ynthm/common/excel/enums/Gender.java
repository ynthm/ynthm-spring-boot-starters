package com.ynthm.common.excel.enums;

import com.ynthm.common.excel.converter.ExcelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum Gender implements ExcelEnum {
  /** 未知 */
  UNKNOWN(-1, "未知"),
  FEMALE(0, "女"),
  MALE(1, "男"),
  ;

  private final int value;

  private final String label;

  public static Gender getByValue(int value) {
    return Arrays.stream(Gender.values())
        .filter(i -> i.getValue() == value)
        .findFirst()
        .orElse(null);
  }

  @Override
  public String getLabel() {
    return label;
  }
}
