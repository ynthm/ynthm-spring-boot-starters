package com.ynthm.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * 为了保护用户的明文密码不被泄露，一般会对密码进行单向不可逆加密——哈希。
 *
 * @author Ethan Wang
 */
public class SecureHashUtil {

  private SecureHashUtil() {}

  private static final Logger LOGGER = LoggerFactory.getLogger(SecureHashUtil.class);

  /** 指定字符集 */
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * SHA-1 (Simplest one – 160 bits Hash) SHA-256 (Stronger than SHA-1 – 256 bits Hash) SHA-384
   * (Stronger than SHA-256 – 384 bits Hash) SHA-512 (Stronger than SHA-384 – 512 bits Hash)
   */
  private static final String ALGORITHM_SHA1 = "SHA-1";

  private static final String ALGORITHM_SHA256 = "SHA-256";

  private static final String ALGORITHM_SHA512 = "SHA-512";

  private static final String ALGORITHM_MD5 = "MD5";

  private static final String ALGORITHM_HMAC_SHA256 = "HmacSHA256";

  public static final String ALGORITHM_SHA1PRNG = "SHA1PRNG";

  public static final String ALGORITHM_PBKDF2 = "PBKDF2WithHmacSHA1";

  public static String sha1(String input) {
    return digest(ALGORITHM_SHA1, input);
  }

  public static String sha1(String input, byte[] salt) {
    return digest(ALGORITHM_SHA1, input, salt);
  }

  public static String sha256(String input) {
    return digest(input, ALGORITHM_SHA256);
  }

  public static String sha256(String input, byte[] salt) {
    return digest(ALGORITHM_SHA256, input, salt);
  }

  public static String sha512(String input) {
    return digest(input, ALGORITHM_SHA512);
  }

  public static String sha512(String input, byte[] salt) {
    return digest(ALGORITHM_SHA512, input, salt);
  }

  /**
   * 字符串md5编码
   *
   * @param input
   * @return
   */
  public static String md5(String input) {
    return md5(input, 32);
  }

  public static String md5(String input, int type) {

    String digest = digest(ALGORITHM_MD5, input);

    if (type == 16) {
      return digest.substring(8, 24);
    }

    return digest;
  }

  public static String md5(String input, byte[] salt) {
    return digest(ALGORITHM_MD5, input, salt);
  }

  public static String salt() {

    try {
      byte[] salt = getSalt();
      return HexUtil.bytes2hex(salt);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("No such algorithm exception", e);
    } catch (NoSuchProviderException e) {
      LOGGER.error("No such provider Exception", e);
    }
    return "";
  }

  public static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
    return getSalt(16);
  }

  public static byte[] getSalt(int length)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    // Always use a SecureRandom generator
    SecureRandom sr = SecureRandom.getInstance(ALGORITHM_SHA1PRNG, "SUN");
    // Create array for salt
    byte[] salt = new byte[length];
    // Get a random salt
    sr.nextBytes(salt);
    // return salt
    return salt;
  }

  public static String hmacSha256(String data, String key) {
    try {
      return hmacSha256(data.getBytes(UTF_8), key.getBytes(UTF_8));
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("UnsupportedEncodingException.", e);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("no such algorithm.", e);
    } catch (InvalidKeyException e) {
      LOGGER.error("invalid key.", e);
    }

    return null;
  }

  public static String hmacSha256(byte[] data, byte[] key)
      throws NoSuchAlgorithmException, InvalidKeyException {
    SecretKeySpec signingKey = new SecretKeySpec(key, ALGORITHM_HMAC_SHA256);
    Mac mac = Mac.getInstance(ALGORITHM_HMAC_SHA256);
    mac.init(signingKey);
    return HexUtil.byte2hex(mac.doFinal(data)).toLowerCase();
  }

  /**
   * 目的是使散列函数足够慢以阻止攻击，但又要足够快以至于不会对用户造成明显的延迟。
   *
   * @param input
   * @param salt
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  public static String hmacsha1PBKDF2(String input, byte[] salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    int iterations = 1000;
    char[] chars = input.toCharArray();

    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_PBKDF2);
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return iterations + ":" + HexUtil.toHex(salt) + ":" + HexUtil.toHex(hash);
  }

  public static boolean validatePBKDF2WithHmacSHA1(String originalPassword, String storedPassword)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    String[] parts = storedPassword.split(":");
    int iterations = Integer.parseInt(parts[0]);
    byte[] salt = HexUtil.fromHex(parts[1]);
    byte[] hash = HexUtil.fromHex(parts[2]);

    PBEKeySpec spec =
        new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_PBKDF2);
    byte[] testHash = skf.generateSecret(spec).getEncoded();

    int diff = hash.length ^ testHash.length;
    for (int i = 0; i < hash.length && i < testHash.length; i++) {
      diff |= hash[i] ^ testHash[i];
    }
    return diff == 0;
  }

  /**
   * 将普通字符串转换成application/x-www-form-urlencoded MIME字符串
   *
   * @return
   */
  public static String urlEncode(String str) throws UnsupportedEncodingException {
    return URLEncoder.encode(str, UTF_8);
  }

  /**
   * 将application/x-www-form-urlencoded MIME字符串转换成普通字符串
   *
   * @return
   */
  public static String urlDecoder(String str) throws UnsupportedEncodingException {
    return URLDecoder.decode(str, UTF_8);
  }

  private static String digest(String algorithm, String input) {
    return digest(algorithm, input, null);
  }

  private static String digest(String algorithm, String input, byte[] salt) {
    if (StringUtil.isBlank(input)) {
      return "";
    }

    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      if (salt != null) {
        md.update(salt);
      }

      byte[] result = md.digest(input.getBytes(UTF_8));
      return HexUtil.bytes2hex(result);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("No such algorithm exception", e);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("Unsupported Encoding Exception", e);
    }

    return "";
  }
}
