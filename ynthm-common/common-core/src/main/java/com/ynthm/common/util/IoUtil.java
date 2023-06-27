package com.ynthm.common.util;

import com.google.common.io.ByteSource;
import com.google.common.io.CharStreams;
import com.ynthm.common.exception.UtilException;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class IoUtil {
  public static final int DEFAULT_BUFFER_SIZE = 8192;

  private IoUtil() {}

  public static String read(InputStream inputStream, Charset cs) {
    try (Reader reader = new InputStreamReader(inputStream, cs)) {
      return CharStreams.toString(reader);
    } catch (IOException e) {
      throw new UtilException(e);
    }
  }

  public static long copy(InputStream inputStream, OutputStream outputStream) {
    ByteSource byteSource =
        new ByteSource() {
          @Override
          public InputStream openStream() throws IOException {
            return inputStream;
          }
        };

    try {
      return byteSource.copyTo(outputStream);
    } catch (IOException e) {
      throw new UtilException(e);
    }
  }

  public static String toString(InputStream inputStream) throws IOException {
    return toString(inputStream, StandardCharsets.UTF_8);
  }

  public static String toString(InputStream inputStream, Charset charset) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
      out.write(buffer, 0, length);
    }

    return out.toString(charset.name());
  }

  public static InputStream toInputStream(String content) {
    return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
  }
}
