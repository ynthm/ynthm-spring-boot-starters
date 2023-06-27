package com.ynthm.common.excel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ynthm.common.excel.annotation.ExcelConstraint;
import lombok.Data;

/**
 * @author Ethan Wang
 */
@Data
public class User {
  @ExcelProperty("名字")
  private String name;

  @ExcelProperty("年龄")
  private Integer age;

  /**
   * ExplicitConstraint excel 下拉框填充
   */
  @ExcelConstraint(source = {"男", "女"})
  @ExcelProperty("性别")
  private String sex;

  @ExcelConstraint(sourceMethod = GenderSelectConstraint.class)
  private String roleName;
}
