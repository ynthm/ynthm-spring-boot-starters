package com.ynthm.common.excel.domain;

import com.ynthm.common.excel.converter.ExcelConstraintInterface;
import com.ynthm.common.excel.enums.Gender;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Ethan Wang
 */
public class GenderSelectConstraint implements ExcelConstraintInterface {

  @Override
  public String[] source(Map<String, Object> params) {
    return Arrays.stream(Gender.values()).map(Gender::getLabel).toArray(String[]::new);
  }
}
