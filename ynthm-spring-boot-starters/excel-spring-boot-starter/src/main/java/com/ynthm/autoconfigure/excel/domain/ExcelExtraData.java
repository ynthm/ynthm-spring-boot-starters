package com.ynthm.autoconfigure.excel.domain;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.handler.WriteHandler;
import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@SuperBuilder
@Data
public class ExcelExtraData<T> {

  /**
   * 数据行对应的实体类型
   *
   * <p>含有 @ExcelProperty 注解的 PO 类型
   */
  private Class<T> head;

  private Integer sheetNo;

  private String sheetName;

  private List<WriteHandler> sheetWriteHandlers;

  private List<Converter<T>> converters;
}
