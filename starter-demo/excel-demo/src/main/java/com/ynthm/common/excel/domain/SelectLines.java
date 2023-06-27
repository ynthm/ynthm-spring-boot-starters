package com.ynthm.common.excel.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class SelectLines {
  /**
   * 
   */
  private int firstRow = 1;

  private int lastRow = 0x10000;
}
