package com.ynthm.autoconfigure.excel.domain;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface CellLocation {
  int getRowIndex();

  void setRowIndex(int rowIndex);

  int getColumnIndex();

  void setColumnIndex(int columnIndex);
}
