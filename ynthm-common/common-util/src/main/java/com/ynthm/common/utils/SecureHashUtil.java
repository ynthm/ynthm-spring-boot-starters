package com.ynthm.common.utils;

import com.ynthm.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.util.encoders.Hex;

/**
 * BouncyCastle就是一个提供了很多哈希算法和加密算法的第三方库。它提供了Java标准库没有的一些算法，例如，RipeMD160哈希算法。
 *
 * <p>为了保护用户的明文密码不被泄露，一般会对密码进行单向不可逆加密——哈希。
 *
 * @author Ethan Wang
 */
@Slf4j
public class SecureHashUtil {

  private SecureHashUtil() {}

  /**
   * password - the password bytes (up to 72 bytes) to use for this invocation. salt - the 128 bit
   * salt to use for this invocation. cost - the bcrypt cost parameter. The cost of the bcrypt
   * function grows as 2^cost. Legal values are 4..31 inclusive.
   *
   * @param password 密码
   * @param salt 加盐
   * @return 加密结果
   */
  public static String bCrypt(String password, byte[] salt) {

    byte[] generate = BCrypt.generate(password.getBytes(Constant.CHARSET_UTF_8), salt, 4);
    return Hex.toHexString(generate);
  }

  /**
   * Scrypt算法的核心思想是“哈希计算需要更大的内存空间和时长”。 P - the bytes of the pass phrase. S - the salt to use for
   * this invocation. N - CPU/Memory cost parameter. Must be larger than 1, a power of 2 and less
   * than 2^(128 * r / 8). r - the block size, must be >= 1. p - Parallelization parameter. Must be
   * a positive integer less than or equal to Integer.MAX_VALUE / (128 * r * 8). dkLen - the length
   * of the key to generate.
   *
   * @param password 密码
   * @param salt 加盐
   * @return 加密结果
   */
  public static String sCrypt(String password, byte[] salt) {

    byte[] generate =
        SCrypt.generate(password.getBytes(Constant.CHARSET_UTF_8), salt, 16384, 8, 8, 32);
    return Hex.toHexString(generate);
  }
}
