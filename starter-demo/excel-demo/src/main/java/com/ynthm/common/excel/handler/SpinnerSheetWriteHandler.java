package com.ynthm.common.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

/**
 * @author Ethan Wang
 */
public class SpinnerSheetWriteHandler implements SheetWriteHandler {

  @Override
  public void beforeSheetCreate(
      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
  }

  @Override
  public void afterSheetCreate(
      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    String[] yesOrNo = new String[] {"是", "否"};
    String[] productTypes = new String[] {"普通商品", "易制爆", "易制毒", "剧毒品", "精神麻醉品", "医用毒性品", "民用爆炸品"};
    String[] campusTypes = new String[] {"南校园", "北校园", "东校园", "珠海区"};
    Map<Integer, String[]> mapDropDown = new HashMap<>();
    mapDropDown.put(3, yesOrNo);
    mapDropDown.put(4, productTypes);
    mapDropDown.put(9, campusTypes);
    
    Sheet sheet = writeSheetHolder.getSheet();
    /// 开始设置下拉框
    DataValidationHelper helper = sheet.getDataValidationHelper();
    for (Map.Entry<Integer, String[]> entry : mapDropDown.entrySet()) {
      // 起始行、终止行、起始列、终止列
      CellRangeAddressList addressList =
          new CellRangeAddressList(1, 1000, entry.getKey(), entry.getKey());
      // 设置下拉框数据
      DataValidationConstraint constraint = helper.createExplicitListConstraint(entry.getValue());
      DataValidation dataValidation = helper.createValidation(constraint, addressList);
      // 处理Excel兼容性问题
      if (dataValidation instanceof XSSFDataValidation) {
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setShowErrorBox(true);
      } else {
        dataValidation.setSuppressDropDownArrow(false);
      }
      sheet.addValidationData(dataValidation);
    }

    // 下面定时样式的
    Row row = sheet.getRow(0);
    Workbook workbook = writeWorkbookHolder.getWorkbook();
    row.setHeight((short) 500);
    for (int i = 0; i < row.getLastCellNum(); i++) {
      sheet.setColumnWidth(i, 5000);
      Cell cell = row.getCell(i);
      cell.setCellStyle(setStyle(workbook));
    }
    row.setHeight((short) (205 * 7));
  }

  /***
   * 设置红色字体样式
   * @param wb
   * @return
   */
  public static CellStyle setStyle(Workbook wb) {
    Font dataFont = wb.createFont();
    dataFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
    dataFont.setFontName("宋体");
    dataFont.setFontHeight((short) 240);
    dataFont.setBold(true);
    dataFont.setFontHeightInPoints((short) 10);
    CellStyle dataStyle = wb.createCellStyle();
    dataStyle.setFont(dataFont);
    dataStyle.setWrapText(true);
    dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    dataStyle.setAlignment(HorizontalAlignment.CENTER);
    return dataStyle;
  }
}
