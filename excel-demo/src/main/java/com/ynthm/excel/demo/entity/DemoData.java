package com.ynthm.excel.demo.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ynthm.excel.demo.enums.GenderEnum;
import com.ynthm.excel.demo.excel.converter.ExcelEnumConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
public class DemoData {
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
  private GenderEnum gender;
}
