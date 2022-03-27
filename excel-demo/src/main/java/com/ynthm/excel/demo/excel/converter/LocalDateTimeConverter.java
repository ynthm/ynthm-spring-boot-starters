package com.ynthm.excel.demo.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 3.0 版本已经不需要了
 *
 * @author Ynthm Wang
 * @version 1.0
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

  /** 不使用{@code @LocalDateTimeFormat}注解指定日期格式时,默认会使用该格式. */
  private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

  @Override
  public Class supportJavaTypeKey() {
    return LocalDateTime.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public LocalDateTime convertToJavaData(
      ReadCellData cellData,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration)
      throws Exception {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
    if (excelContentProperty.getField().isAnnotationPresent(LocalDateTimeFormat.class)) {
      dateTimeFormatter =
          DateTimeFormatter.ofPattern(
              excelContentProperty.getField().getAnnotation(LocalDateTimeFormat.class).value());
    }
    return LocalDateTime.parse(cellData.getStringValue(), dateTimeFormatter);
  }

  @Override
  public WriteCellData convertToExcelData(
      LocalDateTime localDateTime,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration)
      throws Exception {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
    if (excelContentProperty.getField().isAnnotationPresent(LocalDateTimeFormat.class)) {
      dateTimeFormatter =
          DateTimeFormatter.ofPattern(
              excelContentProperty.getField().getAnnotation(LocalDateTimeFormat.class).value());
    }

    return new WriteCellData(localDateTime.format(dateTimeFormatter));
  }
}
