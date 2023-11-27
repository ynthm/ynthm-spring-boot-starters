package com.ynthm.autoconfigure.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ynthm.autoconfigure.excel.domain.ExcelExtraData;
import com.ynthm.autoconfigure.excel.domain.ExportExtraData;
import com.ynthm.autoconfigure.excel.domain.ReadExtraData;
import com.ynthm.autoconfigure.excel.handler.ReadDataListener;
import com.ynthm.common.domain.page.PageReq;
import com.ynthm.common.domain.page.PageResp;
import com.ynthm.common.util.StreamUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * EasyExcel 工具类
 *
 * <p>导出只推荐 xlsx
 *
 * <p>https://easyexcel.opensource.alibaba.com/
 *
 * @author Ethan Wang
 */
@Slf4j
public class ExcelUtil {
  private static final int PER_PAGE_SIZE = 10000;

  /** 每次向EXCEL写入的记录数(查询每页数据大小) 10W */
  private static final Integer PER_WRITE_ROW_COUNT = PER_PAGE_SIZE * 10;

  /** 每个sheet存储的记录数 100W 一个sheet 2003版(本工具不考虑)最大行数是65536行 2007后最多1048576行 */
  private static final Integer PER_SHEET_ROW_COUNT = PER_WRITE_ROW_COUNT * 10;

  private ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;

  public ExcelUtil setExcelType(ExcelTypeEnum excelType) {
    this.excelType = excelType;
    return this;
  }

  /**
   * 20w 以内一个SHEET一次查询导出
   *
   * @param out 目标流
   * @param data 导出数据
   */
  public <T extends Serializable> void write(
      ExcelExtraData<T> extraData, List<T> data, OutputStream out) {
    if (!data.isEmpty()) {

      ExcelWriterSheetBuilder sheetBuilder =
          EasyExcelFactory.write(out, extraData.getHead()).autoCloseStream(Boolean.FALSE).sheet();
      excelWriterSheetBuilder(extraData, sheetBuilder);
      sheetBuilder.doWrite(data);
    }
  }

  public <T extends Serializable> void write(
      ExcelExtraData<T> extraData, Stream<T> data, OutputStream out) {
    try (OutputStream outputStream = out) {
      ExcelWriter excelWriter =
          EasyExcel.write(outputStream, extraData.getHead())
              .autoCloseStream(Boolean.FALSE)
              .excelType(excelType)
              .build();
      ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet();
      excelWriterSheetBuilder(extraData, excelWriterSheetBuilder);

      WriteSheet sheet = excelWriterSheetBuilder.build();
      Stream<List<T>> partition = StreamUtil.partition(data, PER_PAGE_SIZE);

      partition.forEach(list -> excelWriter.write(list, sheet));
      excelWriter.finish();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static <T extends Serializable> void excelWriterSheetBuilder(
      ExcelExtraData<T> extraData, ExcelWriterSheetBuilder outSheetBuilder) {
    if (extraData.getSheetNo() != null) {
      outSheetBuilder.sheetNo(extraData.getSheetNo());
    } else if (extraData.getSheetName() != null) {
      outSheetBuilder.sheetName(extraData.getSheetName());
    }

    Optional.ofNullable(extraData.getSheetWriteHandlers())
        .ifPresent(list -> list.stream().forEach(outSheetBuilder::registerWriteHandler));

    Optional.ofNullable(extraData.getConverters())
        .ifPresent(list -> list.stream().forEach(outSheetBuilder::registerConverter));
  }

  /**
   * 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ReadDataListener}
   *
   * @param extraData 额外信息
   * @param input 导入文件流
   * @param consumer 处理读取数据
   */
  public <T extends Serializable> void read(
      ReadExtraData<T> extraData, InputStream input, Consumer<List<T>> consumer) {
    ExcelReaderBuilder readerBuilder =
        EasyExcel.read(input, extraData.getHead(), new ReadDataListener<>(consumer))
            .headRowNumber(extraData.getHeadRowNumber());
    ExcelReaderSheetBuilder readerSheetBuilder;
    if (extraData.getSheetNo() != null) {
      readerSheetBuilder = readerBuilder.sheet(extraData.getSheetNo());

    } else if (extraData.getSheetName() != null) {
      readerSheetBuilder = readerBuilder.sheet(extraData.getSheetName());
    } else {
      readerSheetBuilder = readerBuilder.sheet();
    }

    readerSheetBuilder.doRead();
  }

  /**
   * 支持大数据量导出 分批使用内存
   *
   * @param out 目标流
   * @param function 条件 -> 结果 通过条件每次查询 {@link ExcelUtil#PER_WRITE_ROW_COUNT} 大小数据
   */
  public <P extends Serializable, T extends Serializable> void export(
      ExportExtraData<T> extraData,
      OutputStream out,
      P param,
      Function<PageReq<P>, PageResp<T>> function) {
    String sheetName = extraData.getSheetName();
    try (ExcelWriter excelWriter =
        EasyExcelFactory.write(out, extraData.getHead()).autoCloseStream(false).build()) {
      // 第一次查询数据库 获取 总数量，剩余数据库查询不需要查询总数
      PageReq<P> pageReq = new PageReq<>();
      int pageIndex = 1;
      pageReq.setPage(pageIndex).setSize(PER_PAGE_SIZE).setParam(param);
      PageResp<T> pageResp = function.apply(pageReq);
      int total = extraData.getTotal();
      int sheetTotalCount =
          total / PER_SHEET_ROW_COUNT + (Math.floorMod(total, PER_SHEET_ROW_COUNT) > 0 ? 1 : 0);
      // 这里注意 如果同一个sheet只要创建一次
      ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet(0);
      excelWriterSheetBuilder(extraData, excelWriterSheetBuilder);
      WriteSheet writeSheet = excelWriterSheetBuilder.build();

      List<T> batchList = pageResp.getRecords();
      int pagesPerBatch = PER_WRITE_ROW_COUNT / PER_PAGE_SIZE;
      int writeTimesInOneSheet = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;
      int totalPageCount = (total + PER_PAGE_SIZE - 1) / PER_PAGE_SIZE;
      for (int sheetIndex = 0; sheetIndex < sheetTotalCount; sheetIndex++) {
        if (sheetIndex != 0) {
          writeSheet =
              EasyExcelFactory.writerSheet(sheetIndex, sheetName + " " + sheetIndex).build();
        }

        for (int sectionInSheet = 0; sectionInSheet < writeTimesInOneSheet; sectionInSheet++) {

          for (int indexInSection = 0;
              indexInSection < pagesPerBatch && pageIndex <= totalPageCount;
              indexInSection++) {
            // 第一次分页跳过
            if (pageIndex == 1 && indexInSection == 0) {
              pageIndex = 2;
              continue;
            }
            // 分页去数据库查询数据
            pageReq.setPage(pageIndex);
            pageReq.setSize(PER_PAGE_SIZE);
            // 后面不用count查询总数
            pageReq.setSearchCount(false);
            batchList.addAll(function.apply(pageReq).getRecords());
            pageIndex++;
          }

          if (!batchList.isEmpty()) {
            excelWriter.write(batchList, writeSheet);
            batchList = new ArrayList<>();
          }
        }

        if (!batchList.isEmpty()) {
          excelWriter.write(batchList, writeSheet);
          batchList = new ArrayList<>();
        }
      }
    }
  }

  /**
   * 模板填充导出
   *
   * @param template 模板输入流
   * @param out 输出流
   * @param function 分组查询数据
   */
  public <P extends Serializable, T extends Serializable> void fill(
      ExportExtraData<T> extraData,
      InputStream template,
      OutputStream out,
      P param,
      Function<PageReq<P>, List<T>> function)
      throws IOException {
    int total = extraData.getTotal();
    int sheets =
        total / PER_SHEET_ROW_COUNT + (Math.floorMod(total, PER_SHEET_ROW_COUNT) > 0 ? 1 : 0);
    if (sheets > 1) {
      template = cloneSheetZero(template, sheets - 1);
    }

    try (ExcelWriter excelWriter =
        EasyExcelFactory.write(out, extraData.getHead())
            .excelType(excelType)
            .withTemplate(template)
            .build()) {
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
        PageReq<P> pageReq = new PageReq<>();
        for (int p = startPage; p < pages; p++) {
          // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
          pageReq.setPage(p + 1);
          pageReq.setSize(PER_WRITE_ROW_COUNT);
          pageReq.setParam(param);
          apply = function.apply(pageReq);
          excelWriter.fill(apply, writeSheet);
        }
      }
    }
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
