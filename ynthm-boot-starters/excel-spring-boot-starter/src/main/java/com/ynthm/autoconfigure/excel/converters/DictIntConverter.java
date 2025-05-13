package com.ynthm.autoconfigure.excel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.ynthm.autoconfigure.excel.annotation.ExcelDictFormat;
import com.ynthm.autoconfigure.excel.domain.IDictCodeLabel;
import com.ynthm.common.web.core.util.SpringContextHolder;
import org.springframework.util.Assert;

/**
 * @author Ethan Wang
 */
public class DictIntConverter implements Converter<Number> {

  @Override
  public Class<Number> supportJavaTypeKey() {
    return Number.class;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return CellDataTypeEnum.NUMBER;
  }

  @Override
  public Number convertToJavaData(
      ReadCellData cellData,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration) {

    ExcelDictFormat annotation =
        excelContentProperty.getField().getAnnotation(ExcelDictFormat.class);

    Assert.notNull(annotation, "枚举值不合法， 请使用 @ExcelDictFormat 指定枚举");
    String parentCode = annotation.parentCode();
    IDictCodeLabel dictCodeLabel = SpringContextHolder.getBean(IDictCodeLabel.class);
    return dictCodeLabel.code(parentCode, cellData.getStringValue());
  }

  @Override
  public WriteCellData<String> convertToExcelData(
      Number t,
      ExcelContentProperty excelContentProperty,
      GlobalConfiguration globalConfiguration) {
    ExcelDictFormat annotation =
        excelContentProperty.getField().getAnnotation(ExcelDictFormat.class);

    Assert.notNull(annotation, "枚举值不合法， 请使用 @ExcelDictFormat 指定枚举");
    String parentCode = annotation.parentCode();
    IDictCodeLabel dictCodeLabel = SpringContextHolder.getBean(IDictCodeLabel.class);
    return new WriteCellData<>(dictCodeLabel.label(parentCode, t.intValue()));
  }
}
