package com.ynthm.autoconfigure.excel.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.google.common.collect.Lists;
import com.ynthm.autoconfigure.excel.handler.ReadDataListener;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class ExcelUtilTest {

  @Test
  void read() throws IOException {
    InputStream inputStream = new ClassPathResource("租户导入模板.xlsx").getInputStream();
    try (ExcelReader excelReader = EasyExcelFactory.read(inputStream).build()) {
      for (ReadSheet readSheet : excelReader.excelExecutor().sheetList()) {
        readSheet.setHeadRowNumber(2);
        readSheet.setCustomReadListenerList(
                Lists.newArrayList(
                        new ReadDataListener<>(
                                list -> {
                                  System.out.println(list.size());
                                })));

        excelReader.read(readSheet);
      }
    }
  }

  @Test
  void read2() throws IOException {
    InputStream inputStream = new ClassPathResource("租户导入模板.xlsx").getInputStream();
    ExcelUtil excelUtil = new ExcelUtil();
    excelUtil.readRowMap(
            2,
            inputStream,
            sheetData -> {
              System.out.println(sheetData.getSheetName());
            });
  }

}
