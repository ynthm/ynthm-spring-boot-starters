package com.ynthm.common.util;

import java.math.BigDecimal;

/**
 * 数字工具类<br>
 * 对于精确值计算应该使用 {@link BigDecimal}<br>
 * JDK7中<strong>BigDecimal(double val)</strong>构造方法的结果有一定的不可预知性，例如：
 *
 * <pre>
 * new BigDecimal(0.1)
 * </pre>
 *
 * <p>表示的不是<strong>0.1</strong>而是<strong>0.1000000000000000055511151231257827021181583404541015625</strong>
 *
 * <p>这是因为0.1无法准确的表示为double。因此应该使用<strong>new BigDecimal(String)</strong>。
 *
 * @author Ethan Wang
 */
public class NumberUtil {
  private NumberUtil() {}

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Character#compare(char, char)
   * @since 3.0.1
   */
  public static int compare(char x, char y) {
    return x - y;
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Double#compare(double, double)
   * @since 3.0.1
   */
  public static int compare(double x, double y) {
    return Double.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Integer#compare(int, int)
   * @since 3.0.1
   */
  public static int compare(int x, int y) {
    return Integer.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Long#compare(long, long)
   * @since 3.0.1
   */
  public static int compare(long x, long y) {
    return Long.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Short#compare(short, short)
   * @since 3.0.1
   */
  public static int compare(short x, short y) {
    return Short.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Byte#compare(byte, byte)
   * @since 3.0.1
   */
  public static int compare(byte x, byte y) {
    return Byte.compare(x, y);
  }

  /**
   * 比较大小，值相等 返回true<br>
   * 此方法通过调用{@link Double#doubleToLongBits(double)}方法来判断是否相等<br>
   * 此方法判断值相等时忽略精度的，即0.00 == 0
   *
   * @param num1 数字1
   * @param num2 数字2
   * @return 是否相等
   * @since 5.4.2
   */
  public static boolean equals(double num1, double num2) {
    return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
  }

  /**
   * 比较大小，值相等 返回true<br>
   * 此方法通过调用{@link Float#floatToIntBits(float)}方法来判断是否相等<br>
   * 此方法判断值相等时忽略精度的，即0.00 == 0
   *
   * @param num1 数字1
   * @param num2 数字2
   * @return 是否相等
   * @since 5.4.5
   */
  public static boolean equals(float num1, float num2) {
    return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
  }

  /**
   * 比较大小，值相等 返回true<br>
   * 此方法通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等<br>
   * 此方法判断值相等时忽略精度的，即0.00 == 0
   *
   * @param bigNum1 数字1
   * @param bigNum2 数字2
   * @return 是否相等
   */
  public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
    //noinspection NumberEquality
    if (bigNum1.equals(bigNum2)) {
      // 如果用户传入同一对象，省略compareTo以提高性能。
      return true;
    }
    if (bigNum1 == null || bigNum2 == null) {
      return false;
    }
    return 0 == bigNum1.compareTo(bigNum2);
  }

  /**
   * 计算等份个数
   *
   * @param total 总数
   * @param part 每份的个数
   * @return 分成了几份
   * @since 3.0.6
   */
  public static int count(int total, int part) {
    return (total % part == 0) ? (total / part) : (total / part + 1);
  }

  /**
   * 空转0
   *
   * @param decimal {@link BigDecimal}，可以为{@code null}
   * @return {@link BigDecimal}参数为空时返回0的值
   * @since 3.0.9
   */
  public static BigDecimal null2Zero(BigDecimal decimal) {

    return decimal == null ? BigDecimal.ZERO : decimal;
  }

  public static int page(int total, int size) {
    return (total + size - 1) / size;
  }
}
