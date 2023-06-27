package com.ynthm.common.util;

import com.google.common.io.BaseEncoding;
import com.ynthm.common.constant.Constant;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * 十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。 *
 * 例如十进制数57，在二进制写作111001，在16进制写作39。 像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 *
 * 0x,比如0x20是十进制的32,而不是十进制的20
 *
 * @author Ynthm Wang
 * @version 1.0
 */
public class HexUtil {

  private HexUtil() {}

  public static String hex(byte[] bytes) {
    return BaseEncoding.base16().lowerCase().encode(bytes);
  }

  private static final char[] DIGITS_LOWER =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
  private static final char[] DIGITS_UPPER =
      new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  /**
   * 判断给定字符串是否为16进制数<br>
   * 如果是，需要使用对应数字类型对象的{@code decode}方法解码<br>
   * 例如：{@code Integer.decode}方法解码int类型的16进制数字
   *
   * @param value 值
   * @return 是否为16进制
   */
  public static boolean isHexNumber(String value) {
    final int index = (value.startsWith("-") ? 1 : 0);
    if (value.startsWith("0x", index)
        || value.startsWith("0X", index)
        || value.startsWith("#", index)) {
      try {
        //noinspection ResultOfMethodCallIgnored
        Long.decode(value);
      } catch (NumberFormatException e) {
        return false;
      }
      return true;
    }

    return false;
  }

  /**
   * 将字符串转换为十六进制字符串，结果为小写，默认编码是UTF-8
   *
   * @param data 被编码的字符串
   * @return 十六进制String
   */
  public static String encodeHexStr(String data) {
    return encodeHexStr(data, Constant.CHARSET_UTF_8);
  }

  /**
   * 将字符串转换为十六进制字符串，结果为小写
   *
   * @param data 需要被编码的字符串
   * @param charset 编码
   * @return 十六进制String
   */
  public static String encodeHexStr(String data, Charset charset) {
    return encodeHexStr(data.getBytes(charset), true);
  }

  /**
   * 将字节数组转换为十六进制字符串
   *
   * @param data byte[]
   * @param toLowerCase {@code true} 传换成小写格式 ， {@code false} 传换成大写格式
   * @return 十六进制String
   */
  public static String encodeHexStr(byte[] data, boolean toLowerCase) {
    return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
  }

  /**
   * 转十六进制
   *
   * @param bytes
   * @return
   */
  public static String bytes2hex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte aByte : bytes) {
      sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }

  public static String byte2hex(byte[] hash) {
    StringBuilder hex = new StringBuilder(hash.length * 2);
    for (byte b : hash) {
      if ((b & 0xFF) < 0x10) {
        hex.append("0");
      }
      hex.append(String.format("%02X", b));
    }
    return hex.toString();
  }

  public static String toHex(byte[] array) {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if (paddingLength > 0) {
      String format = "%0" + paddingLength + "d";
      return String.format(format, 0) + hex;
    } else {
      return hex;
    }
  }

  /**
   * 十六进制字符串转 byte[]
   *
   * @param hex
   * @return
   */
  public static byte[] fromHex(String hex) {
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
    }
    return bytes;
  }

  public static BigInteger toBigInteger(String hexStr) {
    return null == hexStr ? null : new BigInteger(hexStr, 16);
  }

  /**
   * 转为16进制字符串
   *
   * @param value int值
   * @return 16进制字符串
   * @since 4.4.1
   */
  public static String toHex(int value) {
    return Integer.toHexString(value);
  }

  /**
   * 转为16进制字符串
   *
   * @param value int值
   * @return 16进制字符串
   * @since 4.4.1
   */
  public static String toHex(long value) {
    return Long.toHexString(value);
  }

  /**
   * 将字节数组转换为十六进制字符串
   *
   * @param data byte[]
   * @param toDigits 用于控制输出的char[]
   * @return 十六进制String
   */
  private static String encodeHexStr(byte[] data, char[] toDigits) {
    return new String(encodeHex(data, toDigits));
  }

  /**
   * 将字节数组转换为十六进制字符数组
   *
   * @param data byte[]
   * @param toDigits 用于控制输出的char[]
   * @return 十六进制char[]
   */
  private static char[] encodeHex(byte[] data, char[] toDigits) {
    final int len = data.length;
    // len*2
    final char[] out = new char[len << 1];
    // two characters from the hex value.
    for (int i = 0, j = 0; i < len; i++) {
      // 高位
      out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
      // 低位
      out[j++] = toDigits[0x0F & data[i]];
    }
    return out;
  }
}
