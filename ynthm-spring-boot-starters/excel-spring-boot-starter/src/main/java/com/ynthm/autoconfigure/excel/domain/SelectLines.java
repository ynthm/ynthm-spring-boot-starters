package com.ynthm.autoconfigure.excel.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class SelectLines {
  /** 列表从0开始行 去掉表头一般在第二行 */
  private int firstRow = 1;

  private int lastRow = 0x10000;
}
