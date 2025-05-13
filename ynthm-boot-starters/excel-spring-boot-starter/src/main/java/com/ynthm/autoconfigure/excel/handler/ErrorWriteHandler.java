package com.ynthm.autoconfigure.excel.handler;

import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.autoconfigure.excel.domain.RowError;
import com.ynthm.autoconfigure.excel.domain.SheetError;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@RequiredArgsConstructor
public class ErrorWriteHandler implements WorkbookWriteHandler {

  private final int errorColumnIndex;
  private final List<SheetError> sheetErrors;

  @Override
  public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
    for (SheetError sheetError : sheetErrors) {
      Sheet sheet = writeWorkbookHolder.getCachedWorkbook().getSheet(sheetError.getSheetName());
      for (RowError error : sheetError.getErrors()) {
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
