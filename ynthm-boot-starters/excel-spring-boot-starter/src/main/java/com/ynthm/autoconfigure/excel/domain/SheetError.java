package com.ynthm.autoconfigure.excel.domain;

import java.util.List;
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
public class SheetError {
  private String sheetName;
  private List<RowError> errors;
}
