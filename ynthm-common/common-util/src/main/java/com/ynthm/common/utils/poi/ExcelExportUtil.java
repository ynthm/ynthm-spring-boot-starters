package com.ynthm.common.utils.poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 导出
 *
 * <p>https://poi.apache.org/components/spreadsheet/quick-guide.html
 *
 * <p>https://poi.apache.org/components/spreadsheet/how-to.html#sxssf
 *
 * @param <T>
 */
public class ExcelExportUtil<T> {

  public void export() throws IOException {
    String fileName = "sxssf.xlsx";

    // 输出路径
    FileOutputStream fos = new FileOutputStream(new File(fileName));
  }

  public void export(String title, String[] headers, Collection<T> dataset, OutputStream os)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // 每次写100行数据，就刷新数据出缓存
    SXSSFWorkbook wb = new SXSSFWorkbook(100);
    Sheet sheet = wb.createSheet(title);
    sheet.setDefaultColumnWidth(20);

    // 生成一个样式
    CellStyle cellStyle = wb.createCellStyle();
    // 自定义颜色
    XSSFColor xssfColor =
        new XSSFColor(new java.awt.Color(255, 255, 0), new DefaultIndexedColorMap());

    // 设置这些样式
    cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    // 生成一个字体
    Font font = wb.createFont();
    font.setBold(true);
    font.setFontName("微软雅黑");
    font.setColor(HSSFColor.HSSFColorPredefined.PINK.getIndex());
    font.setFontHeightInPoints((short) 11);
    cellStyle.setFont(font);

    // 生成并设置另一个样式
    CellStyle style2 = wb.createCellStyle();
    style2.setFillForegroundColor(xssfColor.getIndex());
    style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style2.setBorderBottom(BorderStyle.THIN);
    style2.setBorderLeft(BorderStyle.THIN);
    style2.setBorderRight(BorderStyle.THIN);
    style2.setBorderTop(BorderStyle.THIN);
    style2.setAlignment(HorizontalAlignment.CENTER);
    style2.setVerticalAlignment(VerticalAlignment.CENTER);
    // 生成另一个字体
    Font font2 = wb.createFont();
    font.setFontName("微软雅黑");
    style2.setFont(font2);

    // 第四个单元区域
    // 第7--12行7--9列合并
    CellRangeAddress region4 = new CellRangeAddress(6, 11, (short) 6, (short) 8);
    sheet.addMergedRegion(region4);
    Row row3 = sheet.createRow(6);
    row3.createCell(6).setCellValue("第7--12行7--9列合并");

    RegionUtil.setBorderBottom(BorderStyle.THIN, region4, sheet);
    RegionUtil.setBorderLeft(BorderStyle.THIN, region4, sheet);
    RegionUtil.setBorderRight(BorderStyle.THIN, region4, sheet);
    RegionUtil.setBorderTop(BorderStyle.THIN, region4, sheet);

    RegionUtil.setBottomBorderColor(12, region4, sheet);
    RegionUtil.setLeftBorderColor(12, region4, sheet);
    RegionUtil.setRightBorderColor(12, region4, sheet);
    RegionUtil.setTopBorderColor(12, region4, sheet);

    // 第三个  第四个合并的单元区域起始行不在同一列，所以必须创建新的Row
    CellUtil.getCell(row3, 6).setCellStyle(cellStyle);

    Cell cell = null;
    Row row1 = sheet.createRow(0);
    for (int i = 0; i < headers.length; i++) {
      cell = row1.createCell(i);
      cell.setCellValue(new XSSFRichTextString(headers[i]));
    }

    Iterator<T> iterator = dataset.iterator();
    int index = 0;
    T rowData;
    Row row;
    Field[] declaredFields;
    String getMethodName;
    Object value;

    while (iterator.hasNext()) {
      index++;
      rowData = iterator.next();
      row = sheet.createRow(index);

      declaredFields = rowData.getClass().getDeclaredFields();

      for (int i = 0; i < declaredFields.length; i++) {
        String filedName = declaredFields[0].getName();
        cell = row.createCell(i);
        getMethodName = "get" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        value =
            rowData
                .getClass()
                .getMethod(getMethodName, new Class[] {})
                .invoke(rowData, new Object[] {});
        if (value instanceof Integer) {
          cell.setCellValue((Integer) value);
        } else if (value instanceof Float) {
          cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
          cell.setCellValue((Double) value);
        } else if (value instanceof Long) {
          cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
          cell.setCellValue((Boolean) value);
        } else {
          cell.setCellValue(value.toString());
        }
      }
    }

    try {
      wb.write(os);
    } catch (IOException e) {

    } finally {
      IOUtils.closeQuietly(os);
      IOUtils.closeQuietly(wb);
    }
    //
    //      try (OutputStream fileOut = new FileOutputStream("workbook.xls")) {
    //          wb.write(fileOut);
    //      }
  }
}
