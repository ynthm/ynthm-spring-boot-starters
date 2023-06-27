package com.ynthm.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ethan Wang
 */
public class RandomUtil {

  public static final char[] CHARS =
      new char[] {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
        '2', '3', '4', '5', '6', '7', '8', '9', '~', '!', '@', '#', '$', '%', '^', '-', '+', '&',
        '_'
      };

  private RandomUtil() {}

  /**
   * 六位随机数
   *
   * @return 六位随机数
   */
  public static String verificationCode() {
    return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
  }

  public static String randomPassword() {

    int length = ThreadLocalRandom.current().nextInt(8, 32);
    int count = 0;
    char[] result = new char[length];
    Random random = new Random(System.nanoTime());
    while (count < length) {
      int i = random.nextInt(CHARS.length);
      result[count++] = CHARS[i];
    }

    return new String(result);
  }

  /**
   * 获取金额
   *
   * @param min 最小
   * @param max 最大
   * @return 中间值
   */
  public static BigDecimal getFloatingPriceBetweenMinAndMax(BigDecimal min, BigDecimal max) {
    float minF = min.floatValue();
    float maxF = max.floatValue();

    // 生成随机数
    BigDecimal db = BigDecimal.valueOf(Math.random() * (maxF - minF) + minF);
    int scale = Math.max(min.scale(), max.scale());

    // 返回保留两位小数的随机数。不进行四舍五入
    return db.setScale(scale, RoundingMode.DOWN);
  }

  /**
   * @param basic 基准
   * @param floatingRange 浮动范围
   * @return 结果
   */
  public static BigDecimal getFloatingPrice(BigDecimal basic, BigDecimal floatingRange) {
    float minF = basic.floatValue();

    // 生成随机数
    BigDecimal db = BigDecimal.valueOf(Math.random() * floatingRange.floatValue() + minF);

    // 返回保留两位小数的随机数。不进行四舍五入
    return db.setScale(basic.scale(), RoundingMode.DOWN);
  }
}
