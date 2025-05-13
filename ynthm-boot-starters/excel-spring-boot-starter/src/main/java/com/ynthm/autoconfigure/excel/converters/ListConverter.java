package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ynthm.common.api.Label;
import com.ynthm.common.exception.BaseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ListConverter implements Converter<List<Label>> {
  @Override
  public Class<?> supportJavaTypeKey() {
    return String.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public List<Label> convertToJavaData(
      ReadCellData<?> cellData,
      ExcelContentProperty contentProperty,
      GlobalConfiguration globalConfiguration) {
    throw new BaseException("not implemented");
  }

  @Override
  public WriteCellData<?> convertToExcelData(
      List<Label> value,
      ExcelContentProperty contentProperty,
      GlobalConfiguration globalConfiguration) {
    return new WriteCellData<>(
        value.stream().map(Label::label).collect(Collectors.joining(", ")));
  }
}
