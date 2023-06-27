package com.ynthm.common.util;

import com.ynthm.common.enums.security.HmacAlgorithm;
import com.ynthm.common.exception.BaseException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Ethan Wang
 */
public class HmacUtil {

  private HmacUtil() {}

  public static byte[] hmacKey(HmacAlgorithm algorithm) {
    try {
      // 创建密钥生成器
      KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getValue());
      // 产生密钥
      SecretKey secretKey = keyGenerator.generateKey();
      // 返回密钥
      return secretKey.getEncoded();
    } catch (NoSuchAlgorithmException e) {
      throw new BaseException(e);
    }
  }

  public static String hexHmacKey(HmacAlgorithm algorithm) {
    return HexUtil.hex(hmacKey(algorithm));
  }

  public static String base64Encrypt(HmacAlgorithm algorithm, String key, String data) {
    return Base64.getEncoder()
        .encodeToString(
            encrypt(
                algorithm,
                key.getBytes(StandardCharsets.UTF_8),
                data.getBytes(StandardCharsets.UTF_8)));
  }

  public static boolean base64Verify(
      HmacAlgorithm algorithm, String key, String data, String signature) {
    return verify(
        algorithm,
        key.getBytes(StandardCharsets.UTF_8),
        data.getBytes(StandardCharsets.UTF_8),
        Base64.getDecoder().decode(signature));
  }

  /**
   * 编码
   *
   * @param algorithm 算法
   * @param key 秘钥 .getBytes(StandardCharsets.UTF_8)
   * @param data 需要编码数据 .getBytes(StandardCharsets.UTF_8)
   * @return
   */
  public static byte[] encrypt(HmacAlgorithm algorithm, byte[] key, byte[] data) {
    try {
      // 还原密钥
      SecretKey secretKey = new SecretKeySpec(key, algorithm.getValue());
      // 创建MAC对象
      Mac mac = Mac.getInstance(algorithm.getValue());
      // 设置密钥
      mac.init(secretKey);
      // 数据加密
      return mac.doFinal(data);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new BaseException(e);
    }
  }

  /**
   * 验证签名
   *
   * @param algorithm 算法
   * @param key .getBytes(StandardCharsets.UTF_8)
   * @param data .getBytes(StandardCharsets.UTF_8)
   * @param signature .getBytes(StandardCharsets.UTF_8)
   * @return
   */
  public static boolean verify(HmacAlgorithm algorithm, byte[] key, byte[] data, byte[] signature) {
    try {
      // 还原密钥
      SecretKey secretKey = new SecretKeySpec(key, algorithm.getValue());
      // 创建MAC对象
      Mac mac = Mac.getInstance(algorithm.getValue());
      // 设置密钥
      mac.init(secretKey);
      // 数据加密
      byte[] result = mac.doFinal(data);
      return Arrays.equals(signature, result);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new BaseException(e);
    }
  }
}
