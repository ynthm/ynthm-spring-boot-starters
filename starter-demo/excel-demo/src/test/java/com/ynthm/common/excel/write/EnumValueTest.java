package com.ynthm.common.excel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ynthm.common.excel.domain.DemoData;
import com.ynthm.common.excel.enums.Gender;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumValueTest {

  /**
   * 最简单的写
   *
   * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
   *
   * <p>2. 直接写即可
   */
  @Test
  public void simpleWrite() throws IOException {
    // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

    // 写法1 JDK8+
    // since: 3.0.0-beta1

    String homePath = System.getProperty("user.home");

    Path folder = Paths.get(homePath, "easy-excel-data");

    if (!Files.exists(folder)) {
      Path directory = Files.createDirectory(folder);
    }

    Path path = Paths.get(folder.toString(), "export-" + System.currentTimeMillis() + ".xlsx");
    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    // 如果这里想使用03 则 传入excelType参数即可

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      EasyExcel.write(outputStream, DemoData.class)
          .sheet(3, "模板")
          .doWrite(
              () -> {
                // 分页查询数据
                return data();
              });
    } catch (IOException e) {
      e.printStackTrace();
    }

    //    // 写法3
    //    fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
    //    // 这里 需要指定写用哪个class去写
    //    try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
    //      WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
    //      excelWriter.write(data(), writeSheet);
    //    }
  }

  @Test
  public void write02() throws IOException {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    if (!Files.exists(folder)) {
      Files.createDirectory(folder);
    }

    Path path = Paths.get(folder.toString(), "export-" + System.currentTimeMillis() + ".xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {

      try (ExcelWriter excelWriter = EasyExcel.write(outputStream, DemoData.class).build()) {

        WriteSheet sheet1 = EasyExcel.writerSheet(0, "sheet 03").build();
        excelWriter.write(data(), sheet1);

        WriteSheet sheet2 = EasyExcel.writerSheet(2, "sheet 02").build();
        excelWriter.write(data(), sheet2);
        // index 存在数据追加名字不会生效
        WriteSheet sheet3 = EasyExcel.writerSheet(0, "sheet 00").build();
        excelWriter.write(data1(), sheet3);

        excelWriter.finish();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    //    // 写法3
    //    fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
    //    // 这里 需要指定写用哪个class去写
    //    try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
    //      WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
    //      excelWriter.write(data(), writeSheet);
    //    }
  }

  public void select() throws IOException {

    String homePath = System.getProperty("user.home");

    Path folder = Paths.get(homePath, "easy-excel-data");

    if (!Files.exists(folder)) {
      Path directory = Files.createDirectory(folder);
    }

    Path path = Paths.get(folder.toString(), "export-" + System.currentTimeMillis() + ".xlsx");
    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    // 如果这里想使用03 则 传入excelType参数即可

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      EasyExcel.write(outputStream, DemoData.class)
          .sheet("模板")
          .doWrite(
              () -> {
                // 分页查询数据
                return data();
              });
    } catch (IOException e) {
      e.printStackTrace();
    }

    //    // 写法3
    //    fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
    //    // 这里 需要指定写用哪个class去写
    //    try (ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build()) {
    //      WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
    //      excelWriter.write(data(), writeSheet);
    //    }
  }

  private List<DemoData> data() {
    List<DemoData> list = new ArrayList<>();
    DemoData demoData = new DemoData();
    demoData.setId(1L);
    demoData.setName("Ethan");
    demoData.setAge(10);
    demoData.setGender(Gender.MALE);
    demoData.setGenderInResp(1);
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }

  private List<DemoData> data1() {
    List<DemoData> list = new ArrayList<>();
    DemoData demoData = new DemoData();
    demoData.setId(1L);
    demoData.setName("Ethan Wang");
    demoData.setAge(10);
    demoData.setGender(Gender.MALE);
    demoData.setGenderInResp(1);
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }

  private List<DemoData> jdk8Time() {
    List<DemoData> list = new ArrayList<>();
    DemoData demoData = new DemoData();
    demoData.setCreateTime(LocalDateTime.now());
    list.add(demoData);
    return list;
  }
}
