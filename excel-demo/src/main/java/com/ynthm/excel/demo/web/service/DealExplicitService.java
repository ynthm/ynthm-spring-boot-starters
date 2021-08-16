package com.ynthm.excel.demo.web.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.excel.demo.excel.excel.ExplicitConstraint;
import com.ynthm.excel.demo.excel.excel.ExplicitInterface;
import com.ynthm.excel.demo.web.client.User;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/** @author Ethan Wang */
public class DealExplicitService {

  public void deal() throws IOException {
    File file = new File("D:/test.xlsx");
    file.createNewFile();
    FileOutputStream fileOutputStream = new FileOutputStream(file);

    // 下拉列表集合
    Map<Integer, String[]> explicitListConstraintMap = new HashMap<>();

    // 循环获取对应列得下拉列表信息
    Field[] declaredFields = User.class.getDeclaredFields();
    for (int i = 0; i < declaredFields.length; i++) {
      Field field = declaredFields[i];
      // 解析注解信息
      ExplicitConstraint explicitConstraint = field.getAnnotation(ExplicitConstraint.class);
      String[] explicitArray = resolveExplicitConstraint(explicitConstraint);
      if (explicitArray != null && explicitArray.length > 0) {
        explicitListConstraintMap.put(i, explicitArray);
      }
    }

    ExcelWriter excelWriter =
        EasyExcel.write(fileOutputStream, User.class)
            .registerWriteHandler(
                new SheetWriteHandler() {
                  @Override
                  public void beforeSheetCreate(
                      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {}

                  @Override
                  public void afterSheetCreate(
                      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                    // 通过sheet处理下拉信息
                    Sheet sheet = writeSheetHolder.getSheet();
                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    explicitListConstraintMap.forEach(
                        (k, v) -> {
                          CellRangeAddressList rangeList = new CellRangeAddressList();
                          CellRangeAddress addr = new CellRangeAddress(1, 1000, k, k);
                          rangeList.addCellRangeAddress(addr);
                          DataValidationConstraint constraint =
                              helper.createExplicitListConstraint(v);
                          DataValidation validation =
                              helper.createValidation(constraint, rangeList);
                          sheet.addValidationData(validation);
                        });
                  }
                })
            .build();
    WriteSheet sheet = EasyExcel.writerSheet().build();
    excelWriter.write(null, sheet).finish();
  }

  /**
   * 解析注解内容 获取下列表信息
   *
   * @param explicitConstraint
   * @return
   */
  public String[] resolveExplicitConstraint(ExplicitConstraint explicitConstraint) {
    if (explicitConstraint == null) {
      return null;
    }
    // 固定下拉信息
    String[] source = explicitConstraint.source();
    if (source.length > 0) {
      return source;
    }
    // 动态下拉信息
    Class<? extends ExplicitInterface>[] classes = explicitConstraint.sourceClass();
    if (classes.length > 0) {
      ExplicitInterface explicitInterface = null;
      try {
        explicitInterface = classes[0].newInstance();
        String[] source1 = explicitInterface.source();
        if (source1.length > 0) {
          return source1;
        }
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
