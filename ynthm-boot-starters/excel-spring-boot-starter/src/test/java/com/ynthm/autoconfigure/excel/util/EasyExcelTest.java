package com.ynthm.autoconfigure.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.localdate.LocalDateNumberConverter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.ynthm.autoconfigure.excel.converters.ExcelEnumConverter;
import com.ynthm.autoconfigure.excel.domain.RowNum;
import com.ynthm.autoconfigure.excel.handler.ReadSheetWithRowNumFailFastListener;
import com.ynthm.common.api.Label;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class EasyExcelTest {

  private static void writeString2Cell(Sheet sheet, int rowNum, int cellNum, String value) {
    Row row = sheet.getRow(rowNum);
    if (row == null) {
      row = sheet.createRow(rowNum);
    }
    Cell cell = row.getCell(cellNum);
    if (cell == null) {
      cell = row.createCell(cellNum);
    }

    cell.setCellValue(value);
  }

  private List<DemoData> datas() {
    List<DemoData> list = ListUtils.newArrayList();
    for (int i = 0; i < 10; i++) {
      DemoData data = new DemoData();
      data.setString("字符串" + i);
      data.setTime(LocalDateTime.now());
      data.setAmount(new BigDecimal("" + i));
      list.add(data);
    }
    return list;
  }

  @Test
  void readByClass() throws IOException {
    InputStream inputStream = new ClassPathResource("房屋信息导入模板.xlsx").getInputStream();
    ExcelReaderBuilder readerBuilder =
        EasyExcel.read(
                inputStream,
                RoomExcelRow.class,
                new ReadSheetWithRowNumFailFastListener<RoomExcelRow>(
                    list -> {
                      for (RoomExcelRow row : list) {
                        System.out.println(row);
                      }
                    }))
            .headRowNumber(1);
    readerBuilder.doReadAll();
  }

  @Test
  void readAndWrite() throws IOException {
    InputStream inputStream = new ClassPathResource("房屋信息导入模板.xlsx").getInputStream();
    int errorColumnIndex = 10;
    File outputFile = Files.newFile("D:\\tmp\\房屋信息导入模板.xlsx");
    try (ExcelWriter excelWriter =
        EasyExcelFactory.write(outputFile)
            .withTemplate(inputStream)
            .registerWriteHandler(
                new WorkbookWriteHandler() {

                  @Override
                  public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
                    Sheet sheet = writeWorkbookHolder.getCachedWorkbook().getSheetAt(0);
                    System.out.println(sheet.getSheetName());
                    //  Workbook workbook = writeWorkbookHolder.getWorkbook();
                    // Sheet sheet = workbook.getSheetAt(0);
                    System.out.println("afterWorkbookDispose");
                    //                    writeString2Cell(sheet, 0, 27, "错误信息");
                    writeString2Cell(sheet, 0, errorColumnIndex, "错误信息");
                    writeString2Cell(sheet, 1, errorColumnIndex, "哈哈");
                    writeString2Cell(sheet, 2, errorColumnIndex, "呵呵🙂");
                    Sheet sheet2 = writeWorkbookHolder.getCachedWorkbook().getSheetAt(1);

                    System.out.println(sheet.getSheetName());
                    //  Workbook workbook = writeWorkbookHolder.getWorkbook();
                    // Sheet sheet = workbook.getSheetAt(0);
                    System.out.println("afterWorkbookDispose");
                    //                    writeString2Cell(sheet, 0, 27, "错误信息");
                    writeString2Cell(sheet2, 0, errorColumnIndex, "错误信息");
                    writeString2Cell(sheet2, 1, errorColumnIndex, "哈哈");
                    writeString2Cell(sheet2, 2, errorColumnIndex, "呵呵🙂");
                  }
                })
            .registerWriteHandler(
                new SheetWriteHandler() {
                  @Override
                  public void afterSheetCreate(
                      WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                    Sheet sheet = writeSheetHolder.getCachedSheet();
                    System.out.println(sheet.getSheetName());
                    writeString2Cell(sheet, 0, errorColumnIndex + 1, "错误信息");
                  }
                })
            .registerWriteHandler(
                new RowWriteHandler() {
                  @Override
                  public void beforeRowCreate(RowWriteHandlerContext context) {
                    System.out.println("beforeRowCreate" + context.getRowIndex());
                  }

                  @Override
                  public void afterRowDispose(
                      WriteSheetHolder writeSheetHolder,
                      WriteTableHolder writeTableHolder,
                      Row row,
                      Integer relativeRowIndex,
                      Boolean isHead) {
                    // 在最后一行写入后
                    System.out.println("afterRowDispose: " + row.getRowNum());
                  }
                })
            .build()) {
      //      WriteSheet writeSheet = EasyExcelFactory.writerSheet(0).build();
      //      excelWriter.write(Lists.newArrayList(), writeSheet);
      //      excelWriter.writeContext();
    }
  }

  public enum Open implements Label {
    TRUE(true, "是"),
    FALSE(false, "否");
    private final Boolean value;
    private final String label;

    Open(Boolean value, String label) {
      this.value = value;
      this.label = label;
    }

    public Boolean getValue() {
      return value;
    }

    @Override
    public String label() {
      return label;
    }
  }

  @Data
  @ExcelIgnoreUnannotated
  public static class RoomExcelRow implements RowNum {
    private int rowNum;

    @ExcelProperty(value = "项目名称")
    private String projectName;

    @ExcelProperty(value = "楼栋名称")
    private String buildingName;

    @ExcelProperty(value = "楼层名称")
    private String floorName;

    @ExcelProperty(value = "房屋编号")
    private String roomNo;

    @ExcelProperty(value = "房屋面积")
    private BigDecimal roomArea;

    @ExcelProperty(value = "是否公开", converter = ExcelEnumConverter.class)
    private Open open;

    @ExcelProperty(value = "生效日期", converter = LocalDateNumberConverter.class)
    private LocalDate effectiveDate;

    @ExcelProperty(value = "房间用途")
    private String roomUsage;

    @ExcelProperty(value = "备注")
    private String remark;

    @Override
    public int getRowNum() {
      return rowNum;
    }

    @Override
    public void setRowNum(int rowNum) {
      this.rowNum = rowNum;
    }
  }

  @Data
  @ExcelIgnoreUnannotated
  public class DemoData {
    @ExcelProperty(value = "字符标题", index = 23)
    private String string;

    @ExcelProperty(value = "时间", index = 24)
    private LocalDateTime time;

    @ExcelProperty(value = "金额", index = 25)
    private BigDecimal amount;
  }
}
