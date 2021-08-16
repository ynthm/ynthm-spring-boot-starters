package com.ynthm.common.util;

import java.util.Base64;

/**
 * 推荐使用 java.util.Base64
 *
 * @author ethan
 */
public class Base64Util {

  public static final String TYPE_PNG_BASE64 = "data:image/png;base64,";
  public static final String TYPE_JPEG_BASE64 = "data:image/jpeg;base64,";

  private Base64Util() {}

  public static String toBase64Image(String fileName, byte[] bytes) {
    if (StringUtil.isBlank(fileName)) {
      return toBase64("", bytes);
    }
    if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
      return toBase64(TYPE_JPEG_BASE64, bytes);
    } else if (fileName.endsWith(".png")) {
      return toBase64(TYPE_PNG_BASE64, bytes);
    }

    return toBase64("", bytes);
  }

  private static String toBase64(String type, byte[] bytes) {
    return type + Base64.getEncoder().encodeToString(bytes);
  }
}
