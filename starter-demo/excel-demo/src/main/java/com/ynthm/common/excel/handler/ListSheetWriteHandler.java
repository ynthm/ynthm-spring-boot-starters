package com.ynthm.common.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.common.excel.annotation.ExcelConstraint;
import com.ynthm.common.excel.annotation.ExcelConstraint4Enum;
import com.ynthm.common.excel.converter.ExcelConstraintInterface;
import com.ynthm.common.excel.converter.ExcelEnum;
import com.ynthm.common.excel.domain.SelectLines;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

/**
 * @author Ethan Wang
 */
public class ListSheetWriteHandler implements SheetWriteHandler {

  private final Class<?> head;

  private final SelectLines selectLines;

  public ListSheetWriteHandler(Class<?> head, SelectLines selectLines) {
    this.head = head;
    this.selectLines = selectLines;
  }

  @Override
  public void beforeSheetCreate(
      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {}

  @Override
  public void afterSheetCreate(
      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    Map<Integer, String[]> column2SourceMap = resolveExplicitConstraint(head, null);

    // 通过sheet处理下拉信息
    Sheet sheet = writeSheetHolder.getSheet();
    DataValidationHelper helper = sheet.getDataValidationHelper();
    column2SourceMap.forEach(
        (k, v) -> {
          CellRangeAddressList rangeList = new CellRangeAddressList();
          CellRangeAddress addr =
              new CellRangeAddress(selectLines.getFirstRow(), selectLines.getLastRow(), k, k);
          rangeList.addCellRangeAddress(addr);
          DataValidationConstraint constraint = helper.createExplicitListConstraint(v);
          DataValidation validation = helper.createValidation(constraint, rangeList);

          // 处理Excel兼容性问题
          if (validation instanceof XSSFDataValidation) {
            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
          } else {
            validation.setSuppressDropDownArrow(false);
          }

          // 阻止输入非下拉选项的值
          validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
          validation.createErrorBox("提示", "请选择下拉选项中的内容");

          sheet.addValidationData(validation);
        });

    //    // 下面定时样式的
    //    Row row = sheet.createRow(0);
    //    Workbook workbook = writeWorkbookHolder.getWorkbook();
    //    row.setHeight((short) 500);
    //    for (int i = 0; i < row.getLastCellNum(); i++) {
    //      sheet.setColumnWidth(i, 5000);
    //      Cell cell = row.getCell(i);
    //      cell.setCellStyle(setStyle(workbook));
    //    }
    //    row.setHeight((short) (205 * 7));
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

  public Map<Integer, String[]> resolveExplicitConstraint(
      Class<?> head, Map<String, Object> params) {

    // 下拉列表集合
    Map<Integer, String[]> explicitListConstraintMap = new HashMap<>();
    // 循环获取对应列得下拉列表信息
    Field[] declaredFields = head.getDeclaredFields();
    for (int i = 0; i < declaredFields.length; i++) {
      Field field = declaredFields[i];
      // 解析注解信息
      String[] explicitArray = resolveExplicitConstraint(field, params);
      if (explicitArray != null && explicitArray.length > 0) {
        explicitListConstraintMap.put(i, explicitArray);
      }
    }

    return explicitListConstraintMap;
  }
  /**
   * 解析注解内容 获取下列表信息
   *
   * @param explicitConstraint
   * @return
   */
  public String[] resolveExplicitConstraint(Field explicitConstraint, Map<String, Object> params) {
    if (explicitConstraint.isAnnotationPresent(ExcelConstraint4Enum.class)) {
      ExcelConstraint4Enum annotation =
          explicitConstraint.getAnnotation(ExcelConstraint4Enum.class);
      Class<? extends Enum<? extends ExcelEnum>> enumFiled = annotation.enumFiled();

      List<String> result = new ArrayList<>();
      for (Enum<? extends ExcelEnum> enumConstant : enumFiled.getEnumConstants()) {
        result.add(((ExcelEnum) enumConstant).label());
      }

      return result.toArray(new String[0]);
    } else if (explicitConstraint.isAnnotationPresent(ExcelConstraint.class)) {
      ExcelConstraint annotation = explicitConstraint.getAnnotation(ExcelConstraint.class);
      // 固定下拉信息
      String[] source = annotation.source();
      if (source.length > 0) {
        return source;
      }

      // 动态下拉信息
      Class<? extends ExcelConstraintInterface> dynamic = annotation.sourceMethod();
      try {
        ExcelConstraintInterface excelSelectDynamic =
            dynamic.getDeclaredConstructor().newInstance();
        return excelSelectDynamic.source(params);
      } catch (InstantiationException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }

    return new String[0];
  }
}
