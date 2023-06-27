package com.ynthm.common.util;

import com.ynthm.common.constant.Constant;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class Base64 {
  private Base64() {}

  public static String encode(String src) {
    return encodeToString(src.getBytes(Constant.CHARSET_UTF_8));
  }

  public static String encodeToString(byte[] src) {
    return java.util.Base64.getEncoder().encodeToString(src);
  }

  public static byte[] encode(byte[] src) {
    return java.util.Base64.getEncoder().encode(src);
  }

  public static byte[] decode(String src) {
    return decode(src.getBytes(Constant.CHARSET_UTF_8));
  }

  public static byte[] decode(byte[] src) {
    return java.util.Base64.getDecoder().decode(src);
  }
}
