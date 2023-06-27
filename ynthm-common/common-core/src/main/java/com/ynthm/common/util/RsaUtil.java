package com.ynthm.common.util;

import com.google.common.base.Strings;
import com.ynthm.common.constant.Constant;
import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.SignatureAlgorithm;
import com.ynthm.common.exception.UtilException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author ETHAN WANG
 */
@Slf4j
public class RsaUtil {

  private RsaUtil() {}

  /** RSA 最大加密明文大小 */
  private static final int MAX_ENCRYPT_BLOCK = 117;
  /** RSA 最大解密密文大小 */
  private static final int MAX_DECRYPT_BLOCK = 128;

  /**
   * 私钥加密方法
   *
   * @param source 源数据
   * @return 加密后
   */
  public static String encryptByPrivateKey(String privateKeyText, String source) {
    Key privateKey = RsaKeyUtil.restorePrivateKey(privateKeyText);
    return encrypt(CipherAlgorithm.RSA_ECB_PKCS1, privateKey, source);
  }

  public static String decryptByPublicKey(String publicKeyText, String cryptoSrc) {
    Key publicKey = RsaKeyUtil.restorePublicKey(publicKeyText);
    return decrypt(publicKey, cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param source 源数据
   * @return 加密后
   */
  public static String encrypt(String publicKeyText, String source) {
    Key publicKey = RsaKeyUtil.restorePublicKey(publicKeyText);
    return encrypt(CipherAlgorithm.RSA_ECB_PKCS1, publicKey, source);
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return 结果
   */
  public static String decrypt(String privateKeyText, String cryptoSrc) {
    Key privateKey = RsaKeyUtil.restorePrivateKey(privateKeyText);
    return decrypt(privateKey, cryptoSrc);
  }

  public static String decrypt(Key privateKey, String cryptoSrc) {
    return decrypt(CipherAlgorithm.RSA_ECB_PKCS1, privateKey, cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param publicKey 公钥
   * @param source 源数据
   * @return 结果
   */
  public static String encrypt(CipherAlgorithm algorithm, Key publicKey, String source) {
    return encrypt(null, algorithm, publicKey, source);
  }

  public static String encrypt(
      String provider, CipherAlgorithm algorithm, Key publicKey, String source) {
    return Base64.encodeToString(
        encryptBytes(provider, algorithm, publicKey, source.getBytes(Constant.CHARSET_UTF_8)));
  }

  public static byte[] encryptBytes(CipherAlgorithm algorithm, Key publicKey, String source) {
    return encryptBytes(null, algorithm, publicKey, source.getBytes(Constant.CHARSET_UTF_8));
  }

  public static byte[] encryptBytes(
      String provider, CipherAlgorithm algorithm, Key publicKey, byte[] source) {
    Cipher cipher = CryptoUtil.getCipher(provider, algorithm, Cipher.ENCRYPT_MODE, publicKey);
    return encryptBytes(cipher, source);
  }

  public static byte[] encryptBytes(Cipher cipher, byte[] source) {
    try {
      return segment(cipher, source, MAX_ENCRYPT_BLOCK);
    } catch (IllegalBlockSizeException | IOException | BadPaddingException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return 结果
   */
  public static String decrypt(CipherAlgorithm algorithm, Key privateKey, String cryptoSrc) {
    return decrypt(null, algorithm, privateKey, Base64.decode(cryptoSrc));
  }

  public static String decrypt(
      String provider, CipherAlgorithm algorithm, Key key, byte[] cryptoSrc) {
    return new String(
        decryptBytes(
            CryptoUtil.getCipher(provider, algorithm, Cipher.DECRYPT_MODE, key), cryptoSrc),
        Constant.CHARSET_UTF_8);
  }

  public static byte[] decryptBytes(Cipher cipher, byte[] cryptoSrc) {
    try {
      return segment(cipher, cryptoSrc, MAX_DECRYPT_BLOCK);
    } catch (IllegalBlockSizeException | IOException | BadPaddingException e) {
      throw new UtilException(e);
    }
  }

  public static String sign(SignatureAlgorithm algorithm, PrivateKey privateKey, String content) {
    return sign(null, algorithm, privateKey, content);
  }

  public static String sign(
      String provider, SignatureAlgorithm algorithm, PrivateKey privateKey, String content) {
    return sign(initSignSignature(provider, algorithm, privateKey), content);
  }

  public static String sign(Signature signature, String content) {
    return Base64.encodeToString(signBytes(signature, content));
  }

  public static byte[] signBytes(
      String provider, SignatureAlgorithm algorithm, PrivateKey privateKey, String content) {
    Signature signature = initSignSignature(provider, algorithm, privateKey);
    return signBytes(signature, content);
  }

  public static byte[] signBytes(Signature signature, String content) {
    try {
      signature.update(content.getBytes(Constant.CHARSET_UTF_8));
      return signature.sign();
    } catch (SignatureException e) {
      throw new UtilException(e);
    }
  }

  public static boolean verify(
      SignatureAlgorithm algorithm, PublicKey publicKey, String signInBase64, String content) {
    return verify(null, algorithm, publicKey, Base64.decode(signInBase64), content);
  }

  public static boolean verify(
      String provider,
      SignatureAlgorithm algorithm,
      PublicKey publicKey,
      byte[] sign,
      String content) {
    Signature signature = initVerifySignature(provider, algorithm, publicKey);
    return verify(signature, sign, content);
  }

  public static boolean verify(Signature signature, byte[] sign, String content) {
    try {
      signature.update(content.getBytes(Constant.CHARSET_UTF_8));
      return signature.verify(sign);
    } catch (SignatureException e) {
      throw new UtilException(e);
    }
  }

  public static byte[] signPss(
      PrivateKey privateKey, AlgorithmParameterSpec param, byte[] content) {
    Signature signature;
    String sigAlgName = "RSASSA-PSS";
    try {
      signature = Signature.getInstance(sigAlgName);
      signature.setParameter(param);
      signature.initSign(privateKey);
      signature.update(content);
      return signature.sign();
    } catch (NoSuchAlgorithmException
        | InvalidAlgorithmParameterException
        | SignatureException
        | InvalidKeyException e) {
      throw new UtilException(e);
    }
  }

  public static boolean verifyPss(
      PublicKey publicKey, AlgorithmParameterSpec param, byte[] sign, byte[] content) {
    Signature signature;
    String sigAlgName = "RSASSA-PSS";
    try {
      signature = Signature.getInstance(sigAlgName);
      signature.setParameter(param);
      signature.initVerify(publicKey);
      signature.update(content);
      return signature.verify(sign);
    } catch (NoSuchAlgorithmException
        | InvalidAlgorithmParameterException
        | SignatureException
        | InvalidKeyException e) {
      throw new UtilException(e);
    }
  }

  private static Signature initSignSignature(
      String provider, SignatureAlgorithm algorithm, PrivateKey privateKey) {
    Signature signature;
    try {
      if (Strings.isNullOrEmpty(provider)) {
        signature = Signature.getInstance(algorithm.getValue());
      } else {
        signature = Signature.getInstance(algorithm.getValue(), provider);
      }
      signature.initSign(privateKey);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
      throw new UtilException(e);
    }
    return signature;
  }

  private static Signature initVerifySignature(
      String provider, SignatureAlgorithm algorithm, PublicKey publicKey) {
    Signature signature;
    try {
      if (Strings.isNullOrEmpty(provider)) {
        signature = Signature.getInstance(algorithm.getValue());
      } else {
        signature = Signature.getInstance(algorithm.getValue(), provider);
      }
      signature.initVerify(publicKey);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
      throw new UtilException(e);
    }
    return signature;
  }

  private static byte[] segment(Cipher cipher, byte[] bytes, int maxBlock)
      throws IOException, IllegalBlockSizeException, BadPaddingException {

    byte[] decryptedData;
    // 执行解密操作
    int inputLen = bytes.length;

    int offSet = 0;
    byte[] cache;
    int i = 0;
    ByteArrayOutputStream out = new ByteArrayOutputStream(inputLen);
    // 对数据分段加密/解密
    while (inputLen - offSet > 0) {
      if (inputLen - offSet > maxBlock) {
        cache = cipher.doFinal(bytes, offSet, maxBlock);
      } else {
        cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
      }
      out.write(cache, 0, cache.length);
      i++;
      offSet = i * maxBlock;
    }
    decryptedData = out.toByteArray();

    out.close();
    return decryptedData;
  }
}
