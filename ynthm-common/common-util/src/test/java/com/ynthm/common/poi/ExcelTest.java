package com.ynthm.common.poi;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class ExcelTest {
  public static void main(String[] args) throws IOException {
    String filePath = "src/main/resources/test-data.xlsx";
    Path p = Paths.get(filePath);
    Path p1 = new File(filePath).toPath();

    Path path1 = Paths.get("/Users/ethan");
    Iterator<Path> iterator = path1.iterator();
    while (iterator.hasNext()) {
      System.out.println(iterator.next().toString());
    }

    System.out.println(path1.toAbsolutePath());
    Stream<Path> list = Files.list(path1);
    list.forEach(
        item -> {
          System.out.println(item.getFileName());
        });

    if (Files.exists(p)) {
      System.out.println("hello");
    }

    FileStore fileStore = Files.getFileStore(p);
    System.out.println(fileStore.getTotalSpace());

    System.out.println(Files.size(p));

    System.out.println(p.toAbsolutePath());

    // FileInputStream fileInputStream = new FileInputStream();
    // ExcelUtil excelUtil = new ExcelUtil("t",1 );

  }
}
