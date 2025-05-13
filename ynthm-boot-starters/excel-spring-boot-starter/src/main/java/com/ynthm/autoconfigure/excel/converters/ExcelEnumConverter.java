package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.ynthm.common.api.Label;
import com.ynthm.common.util.CastUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将枚举字段 导入导出转换
 *
 * @author Ethan Wang
 * @see com.alibaba.excel.annotation.ExcelProperty#converter()
 */
public class ExcelEnumConverter implements Converter<Enum<? extends Label>> {
  public static final Logger log = LoggerFactory.getLogger(ExcelEnumConverter.class);

  @Override
  public Class<?> supportJavaTypeKey() {
    return Enum.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public Enum<? extends Label> convertToJavaData(ReadConverterContext<?> context) {
    String stringValue = context.getReadCellData().getStringValue();
    Class<?> type = context.getContentProperty().getField().getType();
    for (Object enumConstant : type.getEnumConstants()) {
      if (enumConstant instanceof Label
          && ((Label) enumConstant).label().equalsIgnoreCase(stringValue)) {
        return CastUtil.cast(enumConstant);
      }
    }

    log.warn("枚举 {} 需要实现接口 Label 值:{}", type.getName(), stringValue);

    return null;
  }

  @Override
  public WriteCellData<?> convertToExcelData(WriteConverterContext<Enum<? extends Label>> context) {
    Enum<? extends Label> value = context.getValue();

    String result;
    if (value instanceof Label) {
      result = ((Label) value).label();
    } else {
      result = value.name();
      log.warn("枚举 {} 不合法, 需要实现接口 Label", value.getDeclaringClass().getName());
    }

    return new WriteCellData<>(result);
  }
}
