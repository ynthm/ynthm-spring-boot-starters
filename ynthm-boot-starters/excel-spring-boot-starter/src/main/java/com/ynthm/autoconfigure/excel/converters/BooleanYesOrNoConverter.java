package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class BooleanYesOrNoConverter implements Converter<Boolean> {
  public static final String YES = "是";
  public static final String NO = "否";

  @Override
  public Class<?> supportJavaTypeKey() {
    return Boolean.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
    if (cellData.getStringValue() == null) {
      return null;
    }
    return YES.equals(cellData.getStringValue());
  }

  @Override
  public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
    if (value == null) {
      return null;
    }
    return new WriteCellData<>(value ? YES : NO);
  }
}
