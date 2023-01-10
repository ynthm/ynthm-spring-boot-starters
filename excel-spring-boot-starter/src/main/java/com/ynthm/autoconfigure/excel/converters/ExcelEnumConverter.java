package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.ynthm.autoconfigure.excel.enums.IExcelEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ethan Wang
 */
@Slf4j
public class ExcelEnumConverter implements Converter<Enum<? extends IExcelEnum>> {

  @Override
  public Class<Enum> supportJavaTypeKey() {
    return Enum.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public Enum<? extends IExcelEnum> convertToJavaData(ReadConverterContext<?> context)
          throws Exception {
    String stringValue = context.getReadCellData().getStringValue();
    Class<?> type = context.getContentProperty().getField().getType();
    for (Object enumConstant : type.getEnumConstants()) {
      if (enumConstant instanceof IExcelEnum
              && ((IExcelEnum) enumConstant).getLabel().equalsIgnoreCase(stringValue)) {
        return (Enum<? extends IExcelEnum>) enumConstant;
      }
    }

    log.warn("导出实体中的枚举字段 {} 需要实现接口 IExcelEnum 值:{}", type.getName(), stringValue);

    return null;
  }

  @Override
  public WriteCellData<?> convertToExcelData(
          WriteConverterContext<Enum<? extends IExcelEnum>> context) throws Exception {
    Enum<? extends IExcelEnum> value = context.getValue();

    String result;
    if (value instanceof IExcelEnum) {
      result = ((IExcelEnum) value).getLabel();
    } else {
      result = value.name();
      log.warn("枚举 {} 不合法, 需要实现接口 IExcelEnum", value.getDeclaringClass().getName());
    }

    return new WriteCellData<>(result);
  }
}