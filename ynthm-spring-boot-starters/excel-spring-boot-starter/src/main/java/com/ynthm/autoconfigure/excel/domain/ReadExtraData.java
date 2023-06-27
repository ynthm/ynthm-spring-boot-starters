package com.ynthm.autoconfigure.excel.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class ReadExtraData<T> {
  /**
   * 数据行对应的实体类型
   *
   * <p>含有 @ExcelProperty 注解的 PO 类型
   */
  private final Class<T> head;

  /** 优先选 sheetNo 没有就选 sheetName 都没有就默认第一个 sheet */
  private Integer sheetNo;

  private String sheetName;

  private int headRowNumber;
}
