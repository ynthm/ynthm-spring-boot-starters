package com.ynthm.excel.demo.enums;

import com.ynthm.excel.demo.excel.converter.ExcelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum GenderEnum implements ExcelEnum {
  FEMALE(0, "女性"),
  MALE(1, "男性"),
  ;

  private final int value;
  private final String label;
}
