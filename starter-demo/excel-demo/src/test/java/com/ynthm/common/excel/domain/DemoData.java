package com.ynthm.common.excel.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.ynthm.common.excel.annotation.ExcelEnumClass;
import com.ynthm.common.excel.converter.ExcelEnumConverter;
import com.ynthm.common.excel.converter.IntegerEnumConverter;
import com.ynthm.common.excel.enums.Gender;
import com.ynthm.common.excel.enums.Gender4Integer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

  @ExcelEnumClass(Gender4Integer.class)
  @ExcelProperty(value = "gender", converter = IntegerEnumConverter.class)
  private Integer genderInResp;

  @ContentStyle(dataFormat = 2)
  private BigDecimal balance;

  @ExcelProperty(value = "体重KG")
  @NumberFormat("0.0#") // 会以字符串形式生成单元格，要计算的列不推荐
  private BigDecimal weight;
}
