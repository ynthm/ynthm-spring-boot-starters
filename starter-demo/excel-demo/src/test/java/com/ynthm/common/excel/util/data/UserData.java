package com.ynthm.common.excel.util.data;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.ynthm.autoconfigure.excel.annotation.ExcelConstraint4Enum;
import com.ynthm.autoconfigure.excel.annotation.ExcelEnumClass;
import com.ynthm.autoconfigure.excel.converters.ExcelEnumConverter;
import com.ynthm.autoconfigure.excel.converters.IntegerEnumConverter;
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
public class UserData implements Serializable {
  @ExcelProperty(value = "ID")
  private Integer id;

  @ColumnWidth(10)
  @ExcelProperty(value = "年龄")
  private Integer age;

  @ExcelProperty(value = "姓名")
  private String name;

  @ExcelProperty(value = "创建时间")
  @ColumnWidth(25)
  private LocalDateTime createTime;

  @ExcelConstraint4Enum(enumFiled = Gender.class)
  @ExcelProperty(value = "性别", converter = ExcelEnumConverter.class)
  private Gender gender;

  @ExcelConstraint4Enum(enumFiled = Gender.class)
  @ExcelEnumClass(Gender4Integer.class)
  @ExcelProperty(value = "gender", converter = IntegerEnumConverter.class)
  private Integer genderInResp;

  @ContentStyle(dataFormat = 2)
  private BigDecimal balance;

  @ExcelProperty(value = "体重KG")
  @NumberFormat("0.0#") // 会以字符串形式生成单元格，要计算的列不推荐
  private BigDecimal weight;
}
