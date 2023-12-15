package com.ynthm.common.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class MultipartFileUtil {
  private MultipartFileUtil() {}

  public static MultipartFile fromBytes(byte[] bytes, String fieldName, String fileName)
      throws IOException {
    return new MultipartFileDto(
        fieldName,
        fileName,
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        new ByteArrayInputStream(bytes));
  }

  public static MultipartFile fromFile(File file, String fieldName) throws IOException {
    return new MultipartFileDto(
        fieldName,
        file.getName(),
        MediaType.APPLICATION_OCTET_STREAM_VALUE,
        Files.newInputStream(file.toPath()));
  }

  public static MultipartFile fromInputStream(InputStream is, String fieldName, String fileName)
      throws IOException {
    return new MultipartFileDto(fieldName, fileName, MediaType.APPLICATION_OCTET_STREAM_VALUE, is);
  }
}
