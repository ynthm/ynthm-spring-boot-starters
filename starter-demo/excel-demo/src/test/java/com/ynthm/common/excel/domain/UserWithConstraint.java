package com.ynthm.common.excel.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ynthm.common.excel.annotation.ExcelConstraint;
import com.ynthm.common.excel.annotation.ExcelConstraint4Enum;
import com.ynthm.common.excel.converter.ExcelEnumConverter;
import com.ynthm.common.excel.enums.Gender;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class UserWithConstraint {

  @ExcelProperty(value = "姓名")
  private String name;

  @ColumnWidth(10)
  @ExcelProperty(value = "年龄")
  private Integer age;

  @ExcelProperty(value = "创建时间")
  @ColumnWidth(25)
  private LocalDateTime createTime;

  @ExcelConstraint4Enum(enumFiled = Gender.class)
  @ExcelProperty(value = "性别", converter = ExcelEnumConverter.class)
  private Gender gender;

  @ExcelConstraint(sourceMethod = GenderSelectConstraint.class)
  @ExcelProperty(value = "性别1", converter = ExcelEnumConverter.class)
  private Gender gender1;

  @ExcelConstraint(source = {"男", "女"})
  @ExcelProperty(value = "性别2", converter = ExcelEnumConverter.class)
  private Gender gender2;
}
