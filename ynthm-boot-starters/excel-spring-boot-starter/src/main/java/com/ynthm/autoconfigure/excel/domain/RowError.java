package com.ynthm.autoconfigure.excel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RowError {
  private int rowNum;
  private String error;
}