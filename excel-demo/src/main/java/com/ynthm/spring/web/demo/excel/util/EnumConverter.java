package com.ynthm.spring.web.demo.excel.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Objects;

/** @author Ethan Wang */
public class EnumConverter implements Converter<Enum<?>> {
  public static final Logger log = LoggerFactory.getLogger(EnumConverter.class);

  @Override
  public Class<Enum> supportJavaTypeKey() {
    return Enum.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.NUMBER;
  }

  @Override
  public Enum<?> convertToJavaData(
      CellData cellData,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration)
      throws Exception {

    Class<?> enumClazz = getEnumClazz(excelContentProperty);
    Assert.isTrue(enumClazz.isEnum(), "枚举值不合法， 请使用 @ExcelEnumFormat 指定枚举");

    Object[] enumConstants = enumClazz.getEnumConstants();
    for (Object enumConstant : enumConstants) {
      if (enumConstant instanceof IEnum
          && ((IEnum) enumConstant).getLabel().equalsIgnoreCase(cellData.getStringValue())) {
        return (Enum<?>) enumConstant;
      }
    }

    log.warn("枚举 {} 不合法, 需要实现接口 IEnum", enumClazz.getName());
    return null;
  }

  @Override
  public CellData<String> convertToExcelData(
      Enum value,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration)
      throws Exception {
    Class<?> enumClazz = getEnumClazz(excelContentProperty);

    Assert.isTrue(enumClazz.isEnum(), "枚举值不合法， 请使用 @ExcelEnumFormat 指定枚举");

    Object[] enumConstants = enumClazz.getEnumConstants();
    for (Object enumConstant : enumConstants) {
      if (enumConstant instanceof IEnum && enumConstant == value) {
        return new CellData<>(((IEnum) enumConstant).getLabel());
      }
    }

    log.warn("枚举 {} 不合法, 需要实现接口 IEnum", enumClazz.getName());
    return new CellData<>(value.name());
  }

  private Class<?> getEnumClazz(ExcelContentProperty excelContentProperty) {
    Field field = excelContentProperty.getField();
    Class<?> enumClazz = field.getType();
    if (!enumClazz.isEnum()) {
      ExcelEnumFormat annotation = field.getAnnotation(ExcelEnumFormat.class);
      if (!Objects.isNull(annotation)) {
        enumClazz = annotation.value();
      }
    }
    return enumClazz;
  }
}
