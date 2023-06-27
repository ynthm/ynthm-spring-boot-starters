package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.ynthm.autoconfigure.excel.annotation.ExcelEnumClass;
import com.ynthm.autoconfigure.excel.annotation.IntegerEnum;
import com.ynthm.common.exception.BaseException;
import java.util.Arrays;

/**
 * Integer 对应的是枚举值 一般在 DTO 中
 *
 * @see com.alibaba.excel.annotation.ExcelProperty#converter()
 * @author Ethan Wang
 */
public class IntegerEnumConverter implements Converter<Integer> {
  @Override
  public Class<Integer> supportJavaTypeKey() {
    return Integer.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.STRING;
  }

  @Override
  public Integer convertToJavaData(ReadConverterContext<?> context) {
    String stringValue = context.getReadCellData().getStringValue();

    if (stringValue == null) {
      return null;
    }
    Integer result;
    if (context.getContentProperty().getField().isAnnotationPresent(ExcelEnumClass.class)) {
      Class<? extends Enum<?>> enumClazz =
          context.getContentProperty().getField().getAnnotation(ExcelEnumClass.class).value();

      result =
          Arrays.stream(enumClazz.getEnumConstants())
              .filter(item -> item instanceof IntegerEnum)
              .map(i -> (IntegerEnum) i)
              .filter(a -> stringValue.equals(((IntegerEnum) a).label()))
              .findFirst()
              .map(IntegerEnum::value)
              .orElseThrow(
                  () -> {
                    throw new BaseException(
                        String.format("枚举 %s 需要实现接口 IntegerEnumValueLabel", enumClazz));
                  });
    } else {
      throw new BaseException("字段需要注解 @EnumClass");
    }

    return result;
  }

  @Override
  public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
    Integer value = context.getValue();
    if (value == null) {
      return null;
    }
    String result;
    if (context.getContentProperty().getField().isAnnotationPresent(ExcelEnumClass.class)) {
      Class<? extends Enum<?>> enumClazz =
          context.getContentProperty().getField().getAnnotation(ExcelEnumClass.class).value();
      result =
          Arrays.stream(enumClazz.getEnumConstants())
              .filter(item -> item instanceof IntegerEnum)
              .map(i -> (IntegerEnum) i)
              .filter(a -> value.equals(((IntegerEnum) a).value()))
              .findFirst()
              .map(IntegerEnum::label)
              .orElseThrow(
                  () -> {
                    throw new BaseException(
                        String.format("枚举 %s 需要实现接口 IntegerEnumValueLabel", enumClazz));
                  });
    } else {
      throw new BaseException("字段需要注解 @EnumClass");
    }
    return new WriteCellData<>(result);
  }
}
