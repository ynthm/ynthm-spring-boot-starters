package com.ynthm.common.util;

import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.KeyBitLength;
import com.ynthm.common.exception.UtilException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Cipher 类 getInstance 方法需传递一个加密算法的名称作为参数，用来创建对应的 Cipher，其格式为 algorithm/mode/padding，即
 * 算法名称/工作模式/填充方式，例如 AES/CBC/PKCS5Padding。具体有哪些可选的加密方式，可以参考文档：
 *
 * <p>https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 *
 * @author Ynthm Wang
 */
public class AesUtil {

  public static final String ALGORITHM_AES = "AES";

  /** SecureRandom.getInstance("SHA1PRNG", "SUN") */
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private AesUtil() {}

  /**
   * 密码生成 AES要求密钥的长度可以是128位16个字节、192位(25字节)或者256位(32字节), 位数越高, 加密强度自然越大, 但是加密的效率自然会低一 些, 因此要做好衡量
   *
   * @param salt 加盐
   * @param keyBitLength 密钥长度 bits
   * @return 密钥 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] generateAesKey(final String salt, KeyBitLength keyBitLength) {
    Objects.requireNonNull(salt);
    try {
      KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_AES);
      SECURE_RANDOM.setSeed(salt.getBytes());
      kg.init(keyBitLength.getLength(), SECURE_RANDOM);
      // 产生原始对称密钥
      SecretKey secretKey = kg.generateKey();
      // key转换,根据字节数组生成AES密钥
      return secretKey.getEncoded();
    } catch (NoSuchAlgorithmException e) {
      throw new UtilException(e);
    }
  }

  public static byte[] encryptEcb(CipherAlgorithm transformation, byte[] key, byte[] data) {
    return encryptEcb(null, transformation, key, data);
  }

  /**
   * 加密
   *
   * @param data 需要加密的明文
   * @param key 加密用密钥 base64 byte[]
   * @return 加密结果 base64
   */
  public static byte[] encryptEcb(
      String provider, CipherAlgorithm transformation, byte[] key, byte[] data) {
    try {
      Cipher cipher =
          CryptoUtil.getCipher(provider, transformation, Cipher.ENCRYPT_MODE, secretKeySpec(key));
      return cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
  }

  public static SecretKeySpec secretKeySpec(byte[] key) {
    return new SecretKeySpec(key, ALGORITHM_AES);
  }

  public static byte[] decryptEcb(CipherAlgorithm transformation, byte[] key, byte[] data) {
    return decryptEcb(null, transformation, key, data);
  }
  /**
   * 解密 ECB 模式
   *
   * @param data data.getBytes(UTF_8) 如果编码 Base64.getDecoder().decode(data)
   * @param key key.getBytes(UTF_8)
   * @return 解密 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] decryptEcb(
      String provider, CipherAlgorithm transformation, byte[] key, byte[] data) {
    try {
      Cipher cipher =
          CryptoUtil.getCipher(provider, transformation, Cipher.DECRYPT_MODE, secretKeySpec(key));
      return cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 初始向量IV, 初始向量IV的长度规定为128位16个字节, 初始向量的来源为随机生成.
   *
   * @return 结果 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] iv() {
    byte[] bytes = new byte[16];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(bytes);
    return bytes;
  }

  public static byte[] encryptCbc(
      CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] data) {
    return encryptCbc(null, transformation, key, iv, data);
  }
  /**
   * 解密时用到的密钥, 初始向量IV, 加密模式, Padding模式必须和加密时的保持一致, 否则则会解密失败.
   *
   * @param data data.getBytes(UTF_8) 如果编码 Base64.getDecoder().decode(data) 大于16 bytes
   * @param key key.getBytes(UTF_8)
   * @return 解密 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] encryptCbc(
      String provider, CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] data) {
    try {

      Cipher cipher =
          CryptoUtil.getCipher(
              provider,
              transformation,
              Cipher.ENCRYPT_MODE,
              secretKeySpec(key),
              new IvParameterSpec(iv));
      return cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
  }

  public static byte[] decryptCbc(
      CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] data) {
    return decryptCbc(null, transformation, key, iv, data);
  }

  /**
   * @param data data.getBytes(UTF_8) 如果编码 Base64.getDecoder().decode(data)
   * @param key key.getBytes(UTF_8)
   * @return 解密 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] decryptCbc(
      String provider, CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] data) {

    try {
      //  CBC 模式需要用到初始向量参数
      Cipher cipher =
          CryptoUtil.getAesDecryptCbc(provider, transformation, Cipher.DECRYPT_MODE, key, iv);

      return cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
  }

  public static byte[] encryptGcm(
      CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] aad, byte[] data) {
    return encryptGcm(null, transformation, key, iv, aad, data);
  }

  /**
   * @param data data.getBytes(UTF_8) 如果编码 Base64.getDecoder().decode(data)
   * @param key key.getBytes(UTF_8)
   * @return 结果 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] encryptGcm(
      String provider,
      CipherAlgorithm transformation,
      byte[] key,
      byte[] iv,
      byte[] aad,
      byte[] data) {
    byte[] result;
    try {
      Cipher cipher =
          CryptoUtil.getAesDecryptGcm(provider, transformation, Cipher.ENCRYPT_MODE, key, iv);
      cipher.updateAAD(aad);
      result = cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
    return result;
  }

  public static byte[] decryptGcm(
      CipherAlgorithm transformation, byte[] key, byte[] iv, byte[] aad, byte[] data) {
    return decryptGcm(null, transformation, key, iv, aad, data);
  }

  /**
   * @param data data.getBytes(UTF_8) 如果编码 Base64.getDecoder().decode(data)
   * @param key key.getBytes(UTF_8)
   * @return 解密 new String(x, UTF_8) 根据需要 Base64.getEncoder().encodeToString(x)
   */
  public static byte[] decryptGcm(
      String provider,
      CipherAlgorithm transformation,
      byte[] key,
      byte[] iv,
      byte[] aad,
      byte[] data) {
    try {
      Cipher cipher =
          CryptoUtil.getAesDecryptGcm(provider, transformation, Cipher.DECRYPT_MODE, key, iv);
      cipher.updateAAD(aad);
      return cipher.doFinal(data);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new UtilException(e);
    }
  }
}
