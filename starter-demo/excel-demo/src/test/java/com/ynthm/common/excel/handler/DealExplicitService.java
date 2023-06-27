package com.ynthm.common.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.common.excel.annotation.ExcelConstraint;
import com.ynthm.common.excel.converter.ExcelConstraintInterface;
import com.ynthm.common.excel.domain.User;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 */
public class DealExplicitService {

  @Test
  public void deal() throws IOException {
    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
            Paths.get(folder.toString(), "export-select-" + System.currentTimeMillis() + ".xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {

      // 下拉列表集合
      Map<Integer, String[]> explicitListConstraintMap = new HashMap<>();

      // 循环获取对应列得下拉列表信息
      Field[] declaredFields = User.class.getDeclaredFields();
      for (int i = 0; i < declaredFields.length; i++) {
        Field field = declaredFields[i];
        // 解析注解信息
        ExcelConstraint explicitConstraint = field.getAnnotation(ExcelConstraint.class);
        String[] explicitArray = resolveExplicitConstraint(explicitConstraint);
        if (explicitArray != null && explicitArray.length > 0) {
          explicitListConstraintMap.put(i, explicitArray);
        }
      }

      ExcelWriter excelWriter =
              EasyExcel.write(outputStream, User.class)
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
      excelWriter.write((Collection<?>) null, sheet).finish();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    
  }

  /**
   * 解析注解内容 获取下列表信息
   *
   * @param explicitConstraint
   * @return
   */
  public String[] resolveExplicitConstraint(ExcelConstraint explicitConstraint) {
    if (explicitConstraint == null) {
      return null;
    }
    // 固定下拉信息
    String[] source = explicitConstraint.source();
    if (source.length > 0) {
      return source;
    }

    Class<? extends ExcelConstraintInterface> dynamic = explicitConstraint.sourceMethod();
    try {
      ExcelConstraintInterface excelSelectDynamic =
              dynamic.getDeclaredConstructor().newInstance();
      return excelSelectDynamic.source(null);
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
}
