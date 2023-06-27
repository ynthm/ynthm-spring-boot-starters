package com.ynthm.common.excel.fill;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ynthm.common.excel.converter.ExcelEnumConverter;
import com.ynthm.common.excel.enums.Gender;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class FillData {

  private Integer age;

  private String name;

  private LocalDateTime createdTime;

  @ExcelProperty(value = "性别", converter = ExcelEnumConverter.class)
  private Gender gender;
}
