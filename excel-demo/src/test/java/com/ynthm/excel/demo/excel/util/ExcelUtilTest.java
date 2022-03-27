package com.ynthm.excel.demo.excel.util;

import com.ynthm.excel.demo.entity.DemoData;
import com.ynthm.excel.demo.enums.GenderEnum;
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

  private List<DemoData> data() {
    List<DemoData> list = new ArrayList<>();
    DemoData demoData = new DemoData();
    demoData.setId(1L);
    demoData.setName("Ethan");
    demoData.setAge(10);
    demoData.setGender(GenderEnum.MALE);
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }
}
