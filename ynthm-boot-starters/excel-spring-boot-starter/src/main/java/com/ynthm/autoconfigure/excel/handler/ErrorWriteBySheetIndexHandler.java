package com.ynthm.autoconfigure.excel.handler;

import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.autoconfigure.excel.domain.RowError;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@RequiredArgsConstructor
public class ErrorWriteBySheetIndexHandler implements WorkbookWriteHandler {

  private final int errorColumnIndex;
  private final Map<Integer, List<RowError>> sheetIndex2RowErrorList;

  @Override
  public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
    for (Map.Entry<Integer, List<RowError>> entry : sheetIndex2RowErrorList.entrySet()) {
      Sheet sheet = writeWorkbookHolder.getCachedWorkbook().getSheetAt(entry.getKey());
      for (RowError error : entry.getValue()) {
        writeString2Cell(sheet, error.getRowNum(), errorColumnIndex, error.getError());
      }
    }
  }

  private void writeString2Cell(Sheet sheet, int rowNum, int cellNum, String value) {
    Row row = sheet.getRow(rowNum);
    if (row == null) {
      row = sheet.createRow(rowNum);
    }
    Cell cell = row.getCell(cellNum);
    if (cell == null) {
      cell = row.createCell(cellNum);
    }

    cell.setCellValue(value);
  }
}
