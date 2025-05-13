package com.ynthm.autoconfigure.excel.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class ReadMayWriteErrorData<T> {
  /**
   * 数据行对应的实体类型
   *
   * <p>含有 @ExcelProperty 注解的 PO 类型
   */
  private final Class<T> head;

  /** 从 1 开始 */
  private int headRowNumber = 1;

  private int sheetIndex = 0;
}
