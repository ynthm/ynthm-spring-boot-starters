package com.ynthm.excel.demo.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Ethan Wang */
public class ExcelEnumConverter implements Converter<Enum<? extends ExcelEnum>> {
  public static final Logger log = LoggerFactory.getLogger(ExcelEnumConverter.class);

  @Override
  public Class<Enum> supportJavaTypeKey() {
    return Enum.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public Enum<? extends ExcelEnum> convertToJavaData(ReadConverterContext<?> context)
      throws Exception {
    String stringValue = context.getReadCellData().getStringValue();
    Class<?> type = context.getContentProperty().getField().getType();
    for (Object enumConstant : type.getEnumConstants()) {
      if (enumConstant instanceof ExcelEnum
          && ((ExcelEnum) enumConstant).getLabel().equalsIgnoreCase(stringValue)) {
        return (Enum<? extends ExcelEnum>) enumConstant;
      }
    }

    log.warn("枚举 {} 需要实现接口 ExcelEnum 值:{}", type.getName(), stringValue);

    return null;
  }

  @Override
  public WriteCellData<?> convertToExcelData(
      WriteConverterContext<Enum<? extends ExcelEnum>> context) throws Exception {
    Enum<? extends ExcelEnum> value = context.getValue();

    String result;
    if (value instanceof ExcelEnum) {
      result = ((ExcelEnum) value).getLabel();
    } else {
      result = value.name();
      log.warn("枚举 {} 不合法, 需要实现接口 ExcelEnum", value.getDeclaringClass().getName());
    }

    return new WriteCellData<>(result);
  }
}
