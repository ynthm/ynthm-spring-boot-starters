package com.ynthm.common.excel.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ynthm.common.excel.converter.ExcelEnumConverter;
import com.ynthm.common.excel.domain.SelectLines;
import com.ynthm.common.excel.domain.UserWithConstraint;
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
class ListSheetWriteHandlerTest {

  @Test
  void test1() throws IOException {

    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path =
        Paths.get(folder.toString(), "export-list-select-" + System.currentTimeMillis() + ".xlsx");

    try (OutputStream outputStream = Files.newOutputStream(path)) {

      WriteSheet writeSheet =
          EasyExcel.writerSheet(0, "sheet1")
              .head(UserWithConstraint.class)
              .registerConverter(new ExcelEnumConverter())
              .registerWriteHandler(
                  new ListSheetWriteHandler(
                      UserWithConstraint.class, new SelectLines().setLastRow(data().size())))
              .build();
      ExcelWriter excelWriter = EasyExcel.write(outputStream).build();

      excelWriter.write(data(), writeSheet).finish();
    }
  }

  private List<UserWithConstraint> data() {
    List<UserWithConstraint> list = new ArrayList<>();
    list.add(
        new UserWithConstraint()
            .setName("Ethan")
            .setAge(18)
            .setGender(Gender.MALE)
            .setCreateTime(LocalDateTime.now()));
    list.add(
        new UserWithConstraint()
            .setName("Ynthm")
            .setAge(19)
            .setGender(Gender.MALE)
            .setGender1(Gender.MALE)
            .setGender2(Gender.MALE)
            .setCreateTime(LocalDateTime.now()));
    return list;
  }
}
