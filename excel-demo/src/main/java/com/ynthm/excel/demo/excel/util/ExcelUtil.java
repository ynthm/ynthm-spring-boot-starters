package com.ynthm.excel.demo.excel.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ynthm.common.api.Page;
import com.ynthm.excel.demo.excel.excel.ReadDataListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * EasyExcel 工具类
 *
 * <p>导出只推荐 xlsx
 *
 * <p>https://www.yuque.com/easyexcel/doc
 *
 * @author ethan wang
 * @param <T>
 */
@Slf4j
public class ExcelUtil<T> {
  /** 每个sheet存储的记录数 100W 一个sheet 2003版最大行数是65536行 2007后最多1048576行 */
  public static final Integer PER_SHEET_ROW_COUNT = 1000000;

  /** 每次向EXCEL写入的记录数(查询每页数据大小) 20W */
  public static final Integer PER_WRITE_ROW_COUNT = 200000;

  private int headRowNumber = 1;

  /** 数据行对应的实体类型 */
  private final Class<T> clazz;

  public ExcelUtil(Class<T> clazz) {
    this.clazz = clazz;
  }

  public ExcelUtil<T> setHeadRowNumber(int headRowNumber) {
    this.headRowNumber = headRowNumber;
    return this;
  }

  /**
   * 20w 以内一个SHEET一次查询导出
   *
   * @param out 目标流
   * @param data 导出数据
   */
  public void write(
      String sheetName, OutputStream out, List<T> data, SheetWriteHandler... sheetWriteHandlers) {
    if (!data.isEmpty()) {

      ExcelWriterSheetBuilder sheetBuilder =
          EasyExcelFactory.write(out, clazz).autoCloseStream(Boolean.FALSE).sheet(sheetName);
      if (null != sheetWriteHandlers && sheetWriteHandlers.length > 0) {
        for (SheetWriteHandler sheetWriteHandler : sheetWriteHandlers) {
          sheetBuilder.registerWriteHandler(sheetWriteHandler);
        }
      }
      sheetBuilder.doWrite(data);
    }
  }

  /**
   * 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ReadDataListener}
   *
   * @param input 导入文件流
   * @param consumer 处理读取数据
   */
  public void read(InputStream input, Consumer<List<T>> consumer) {
    EasyExcelFactory.read(input, clazz, new ReadDataListener<>(consumer))
        .sheet()
        .headRowNumber(headRowNumber)
        .doRead();
  }

  /**
   * 支持大数据量导出 分批使用内存
   *
   * @param out 目标流
   * @param total 总数据量
   * @param function 条件 -> 结果 通过条件每次查询 {@link ExcelUtil#PER_WRITE_ROW_COUNT} 大小数据
   */
  public void write(
      String sheetName, OutputStream out, int total, Function<Page<T>, List<T>> function) {
    ExcelWriter excelWriter = EasyExcelFactory.write(out, clazz).build();
    // 这里注意 如果同一个sheet只要创建一次
    WriteSheet writeSheet = EasyExcelFactory.writerSheet(sheetName).build();
    int pages = total / PER_WRITE_ROW_COUNT;
    List<T> apply;
    Page<T> page = new Page<>();
    for (int i = 0; i < pages; i++) {
      // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
      page.setCurrent(i + 1);
      page.setSize(PER_WRITE_ROW_COUNT);
      apply = function.apply(page);
      excelWriter.write(apply, writeSheet);
    }
    // 关闭流
    excelWriter.finish();
  }

  /**
   * 模板填充导出
   *
   * @param template 模板输入流
   * @param out 输出流
   * @param total 总记录条数
   * @param function 分组查询数据
   */
  public void fill(
      InputStream template, OutputStream out, int total, Function<Page<T>, List<T>> function)
      throws IOException {
    int sheets =
        total / PER_SHEET_ROW_COUNT + (Math.floorMod(total, PER_SHEET_ROW_COUNT) > 0 ? 1 : 0);
    if (sheets > 1) {
      template = cloneSheetZero(template, sheets - 1);
    }

    ExcelWriter excelWriter = EasyExcelFactory.write(out, clazz).withTemplate(template).build();
    // 这里注意 如果同一个sheet只要创建一次   0开始
    WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();

    for (int i = 1; i <= sheets; i++) {
      if (i != 1) {
        writeSheet = EasyExcelFactory.writerSheet(i - 1).build();
      }

      int startPage = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT * (i - 1);
      int pages = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT * i;
      if (i == sheets) {
        pages =
            total / PER_WRITE_ROW_COUNT + (Math.floorMod(total, PER_WRITE_ROW_COUNT) > 0 ? 1 : 0);
      }

      List<T> apply;
      Page<T> page = new Page<>();
      for (int p = startPage; p < pages; p++) {
        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        page.setCurrent(p + 1);
        page.setSize(PER_WRITE_ROW_COUNT);
        apply = function.apply(page);
        excelWriter.fill(apply, writeSheet);
      }
    }

    // 关闭流
    excelWriter.finish();
  }

  /**
   * 文件名防止重复
   *
   * @param fileName 文件名不带后缀
   * @return 文件名
   */
  public String encodingFileName(String fileName) {
    return fileName + "-" + Instant.now().toEpochMilli() + ExcelTypeEnum.XLSX.getValue();
  }

  private InputStream cloneSheetZero(InputStream fileInputStream, int count) throws IOException {
    ByteArrayInputStream result;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
      String sheetName = workbook.getSheetName(0);
      for (int i = 0; i < count; i++) {
        XSSFSheet newSheet = workbook.cloneSheet(0);
        int newSheetIndex = workbook.getSheetIndex(newSheet);
        workbook.setSheetName(newSheetIndex, sheetName + newSheetIndex);
      }

      outputStream.flush();
      workbook.write(outputStream);
      result = new ByteArrayInputStream(outputStream.toByteArray());
    }
    return result;
  }
}
