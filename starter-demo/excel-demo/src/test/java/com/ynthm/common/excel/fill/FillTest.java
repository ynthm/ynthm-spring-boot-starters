package com.ynthm.common.excel.fill;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.ynthm.common.excel.enums.Gender;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class FillTest {

  @Test
  public void simpleFill() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path = Paths.get(folder.toString(), "fill-" + System.currentTimeMillis() + ".xlsx");

    InputStream resourceAsStream =
        this.getClass()
            .getClassLoader()
            .getResourceAsStream("template" + File.separator + "simple.xlsx");

    InputStream resourceAsStream1 =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "simple.xlsx");

    // 这里 会填充到第一个sheet， 然后文件流会自动关闭 (用 Map 也行)
    FillData fillData = new FillData();
    fillData.setName("张三");
    fillData.setAge(18);
    fillData.setGender(Gender.MALE);
    fillData.setCreatedTime(LocalDateTime.now());

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      EasyExcel.write(outputStream).withTemplate(resourceAsStream).sheet().doFill(fillData);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void listFill() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path = Paths.get(folder.toString(), "fill-list-" + System.currentTimeMillis() + ".xlsx");

    InputStream resourceAsStream =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "fill-list.xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      //  分多次 填充 会使用文件缓存（省内存）
      try (ExcelWriter excelWriter =
          EasyExcel.write(outputStream).withTemplate(resourceAsStream).build()) {
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data(), writeSheet);
        excelWriter.fill(data(), writeSheet);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void fillComplexTest() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
        Paths.get(folder.toString(), "fill-complex-" + System.currentTimeMillis() + ".xlsx");

    InputStream resourceAsStream =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "fill-complex.xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      //  分多次 填充 会使用文件缓存（省内存）
      try (ExcelWriter excelWriter =
          EasyExcel.write(outputStream).withTemplate(resourceAsStream).build()) {
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认
        // 是false，会直接使用下一行，如果没有则创建。
        // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
        // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
        // 如果数据量大 list不是最后一行 参照下一个
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        excelWriter.fill(data(), fillConfig, writeSheet);
        excelWriter.fill(data(), fillConfig, writeSheet);
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("today", "2019年10月9日13:28:28");
        map.put("total", 1000);
        excelWriter.fill(map, writeSheet);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 数据量大的复杂填充
   *
   * <p>这里的解决方案是 确保模板list为最后一行，然后再拼接table.还有03版没救，只能刚正面加内存。
   */
  @Test
  public void fillHugeDataTest() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
        Paths.get(folder.toString(), "fill-huge-data-" + System.currentTimeMillis() + ".xlsx");

    InputStream resourceAsStream =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "fill-huge-data.xlsx");

    Stream<FillData> stream = Stream.empty();

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      //  分多次 填充 会使用文件缓存（省内存）
      try (ExcelWriter excelWriter =
          EasyExcel.write(outputStream).withTemplate(resourceAsStream).build()) {
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        // 直接写入数据
        excelWriter.fill(data(), writeSheet);
        excelWriter.fill(data(), writeSheet);

        // 写入list之前的数据
        Map<String, Object> map = new HashMap<>();
        map.put("today", "2019年10月9日13:28:28");
        excelWriter.fill(map, writeSheet);

        // list 后面还有个统计 想办法手动写入
        // 这里偷懒直接用list 也可以用对象
        List<List<String>> totalListList = ListUtils.newArrayList();
        List<String> totalList = ListUtils.newArrayList();
        totalListList.add(totalList);
        totalList.add(null);
        totalList.add(null);
        totalList.add(null);
        // 第四列
        totalList.add("统计:1000");
        // 这里是write 别和fill 搞错了
        excelWriter.write(totalListList, writeSheet);
        // 总体上写法比较复杂 但是也没有想到好的版本 异步的去写入excel 不支持行的删除和移动，也不支持备注这种的写入，所以也排除了可以
        // 新建一个 然后一点点复制过来的方案，最后导致list需要新增行的时候，后面的列的数据没法后移，后续会继续想想解决方案
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void fillMultiListTest() {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
        Paths.get(folder.toString(), "fill-multi-list" + System.currentTimeMillis() + ".xlsx");

    InputStream resourceAsStream =
        Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("template" + File.separator + "fill-multi-list.xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {
      //  分多次 填充 会使用文件缓存（省内存）
      try (ExcelWriter excelWriter =
          EasyExcel.write(outputStream).withTemplate(resourceAsStream).build()) {
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig =
            FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
        // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
        excelWriter.fill(new FillWrapper("c", data()), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("c", data()), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("a", data()), writeSheet);
        excelWriter.fill(new FillWrapper("a", data()), writeSheet);
        excelWriter.fill(new FillWrapper("b", data()), writeSheet);
        excelWriter.fill(new FillWrapper("b", data()), writeSheet);

        Map<String, Object> map = new HashMap<>();
        // map.put("date", "2019年10月9日13:28:28");
        map.put("date", LocalDateTime.now());

        excelWriter.fill(map, writeSheet);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<FillData> data() {
    List<FillData> list = new ArrayList<>();
    FillData data = new FillData();
    data.setName("Ethan");
    data.setAge(18);
    data.setGender(Gender.MALE);
    data.setCreatedTime(LocalDateTime.now());
    list.add(data);
    return list;
  }
}
