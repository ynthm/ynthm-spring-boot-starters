package com.ynthm.common.excel.util;

import com.google.common.collect.Lists;
import com.ynthm.autoconfigure.excel.domain.ExcelExtraData;
import com.ynthm.autoconfigure.excel.domain.ExportExtraData;
import com.ynthm.autoconfigure.excel.domain.ReadExtraData;
import com.ynthm.autoconfigure.excel.domain.SelectLines;
import com.ynthm.autoconfigure.excel.handler.ListSheetWriteHandler;
import com.ynthm.autoconfigure.excel.util.ExcelUtil;
import com.ynthm.common.domain.PageResp;
import com.ynthm.common.excel.util.data.Gender;
import com.ynthm.common.excel.util.data.Gender4Integer;
import com.ynthm.common.excel.util.data.UserData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class ExcelUtilTest {

  @Test
  void setHeadRowNumber() {}

  @Test
  void write() {
    ExcelUtil excelUtil = new ExcelUtil();
    String homePath = System.getProperty("user.home");
    Path path = Paths.get(homePath, "easy-excel-data", "export.xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      excelUtil.write(
          ExcelExtraData.<UserData>builder()
              .sheetName("sheet1")
              .sheetWriteHandlers(
                  Lists.newArrayList(new ListSheetWriteHandler(UserData.class, new SelectLines())))
              .build(),
          data(),
          outputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void read() {
    String homePath = System.getProperty("user.home");
    Path path = Paths.get(homePath, "easy-excel-data", "export.xlsx");

    ExcelUtil objectExcelUtil = new ExcelUtil();
    try (InputStream inputStream = Files.newInputStream(path)) {
      objectExcelUtil.read(
          new ReadExtraData<>(UserData.class),
          inputStream,
          list -> {
            for (UserData t : list) {
              System.out.println(t);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void export() {
    ExcelUtil excelUtil = new ExcelUtil();
    String homePath = System.getProperty("user.home");
    Path path = Paths.get(homePath, "easy-excel-data", "export.xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      excelUtil.export(
          ExportExtraData.<UserData>builder()
              .head(UserData.class)
              .sheetName("sheet1")
              .total(10000 * 1000)
              .sheetWriteHandlers(
                  Lists.newArrayList(
                      new ListSheetWriteHandler(
                          UserData.class, new SelectLines().setFirstRow(3).setLastRow(100))))
              .build(),
          outputStream,
          new UserData(),
          page -> {
            System.out.println(page.getPage());
            return new PageResp<UserData>().setRecords(data());
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void fillHugeDataTest() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
        Paths.get(folder.toString(), "fill-huge-data-" + System.currentTimeMillis() + ".xlsx");

    InputStream template =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "fill-huge-data.xlsx");

    ExcelUtil excelUtil = new ExcelUtil();

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      excelUtil.fill(
          ExportExtraData.<UserData>builder()
              .head(UserData.class)
              .sheetName("sheet1")
              .total(10000 * 1000)
              .build(),
          template,
          outputStream,
          page -> {
            return data();
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private AtomicInteger atomicInteger = new AtomicInteger(0);

  private List<UserData> data() {
    List<UserData> list = new ArrayList<>();
    UserData demoData = new UserData();
    demoData.setId(atomicInteger.getAndIncrement());
    demoData.setName("Ethan");
    demoData.setAge(10);
    demoData.setGender(Gender.MALE);
    demoData.setGenderInResp(Gender4Integer.MALE.value());
    demoData.setWeight(new BigDecimal("180.0001"));
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }
}
