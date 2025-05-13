package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ynthm.autoconfigure.excel.util.ExcelTimeUtil;
import java.time.OffsetDateTime;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class OffsetDateTimeConverter implements Converter<OffsetDateTime> {
  @Override
  public Class<?> supportJavaTypeKey() {
    return OffsetDateTime.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public OffsetDateTime convertToJavaData(
      ReadCellData<?> cellData,
      ExcelContentProperty contentProperty,
      GlobalConfiguration globalConfiguration) {
    if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
      return ExcelTimeUtil.parseOffsetDateTime(
          cellData.getStringValue(), null, globalConfiguration.getLocale());
    } else {
      return ExcelTimeUtil.parseOffsetDateTime(
          cellData.getStringValue(),
          contentProperty.getDateTimeFormatProperty().getFormat(),
          globalConfiguration.getLocale());
    }
  }

  @Override
  public WriteCellData<?> convertToExcelData(
      OffsetDateTime value,
      ExcelContentProperty contentProperty,
      GlobalConfiguration globalConfiguration) {
    if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
      return new WriteCellData<>(
          ExcelTimeUtil.format(value, null, globalConfiguration.getLocale()));
    } else {
      return new WriteCellData<>(
          ExcelTimeUtil.format(
              value,
              contentProperty.getDateTimeFormatProperty().getFormat(),
              globalConfiguration.getLocale()));
    }
  }
}
