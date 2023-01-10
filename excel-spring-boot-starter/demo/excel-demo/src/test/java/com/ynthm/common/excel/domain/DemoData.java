package com.ynthm.common.excel.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ynthm.common.excel.converter.ExcelEnumConverter;
import com.ynthm.common.excel.enums.Gender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@ExcelIgnoreUnannotated
@ColumnWidth(15)
public class DemoData implements Serializable {
  @ExcelProperty(value = "ID")
  private Long id;

  @ColumnWidth(10)
  @ExcelProperty(value = "年龄")
  private Integer age;

  @ExcelProperty(value = "姓名")
  private String name;

  @ExcelProperty(value = "创建时间")
  @ColumnWidth(25)
  private LocalDateTime createTime;

  @ExcelProperty(value = "性别", converter = ExcelEnumConverter.class)
  private Gender gender;
}
