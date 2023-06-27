package com.ynthm.common.excel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.ynthm.common.excel.domain.DemoData;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ReadTest {
  

  @Test
  void read() {
    String homePath = System.getProperty("user.home");
    Path folder = Paths.get(homePath, "easy-excel-data");
    Path path = Paths.get(folder.toString(), "export-1686796361415.xlsx");
    try (InputStream inputStream = Files.newInputStream(path)) {
      EasyExcel.read(
              inputStream,
              DemoData.class,
              new PageReadListener<DemoData>(
                  dataList -> {
                    for (DemoData demoData : dataList) {
                      System.out.println(demoData);
                    }
                  }))
          .sheet()
          .doRead();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
