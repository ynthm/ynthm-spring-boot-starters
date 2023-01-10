package com.ynthm.common.excel.util;

import com.ynthm.common.excel.domain.DemoData;
import com.ynthm.common.excel.enums.Gender;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class ExcelUtilTest {

  @Test
  void setHeadRowNumber() {}

  @Test
  void write() {
    ExcelUtil<DemoData> objectExcelUtil = new ExcelUtil<>(DemoData.class);
    try (OutputStream outputStream = Files.newOutputStream(Paths.get("/tmp/test.xlsx"))) {
      objectExcelUtil.write("demo data", outputStream, data());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void read() {
    ExcelUtil<DemoData> objectExcelUtil = new ExcelUtil<>(DemoData.class);
    try (InputStream inputStream = Files.newInputStream(Paths.get("/tmp/test.xlsx"))) {
      objectExcelUtil.read(
          inputStream,
          list -> {
            for (DemoData demoData : list) {
              //
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void export() {}

  @Test
  void fill() {}

  private List<DemoData> data() {
    List<DemoData> list = new ArrayList<>();
    DemoData demoData = new DemoData();
    demoData.setId(1L);
    demoData.setName("Ethan");
    demoData.setAge(10);
    demoData.setGender(Gender.MALE);
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }
}
