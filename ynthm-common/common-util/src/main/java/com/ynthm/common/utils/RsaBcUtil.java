package com.ynthm.common.utils;

import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.SignatureAlgorithm;
import com.ynthm.common.util.Base64;
import com.ynthm.common.util.RsaKeyUtil;
import com.ynthm.common.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
public class RsaBcUtil {

  private RsaBcUtil() {}

  public static final String BC_PROVIDER = "BC";

  public static final String KEY_ALGORITHM = "RSA";

  public static final String SIGN_TYPE = "SHA1withRSA";

  public static final String PKCS12 = "PKCS12";
  public static final String X509 = "X.509";

  /** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
  public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

  /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
  public static final int KEY_SIZE = 1024;

  /** RSA最大加密明文大小 */
  private static final int MAX_ENCRYPT_BLOCK = 117;
  /** RSA最大解密密文大小 */
  private static final int MAX_DECRYPT_BLOCK = 128;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
   *
   * @return
   */
  public static void generateKeyPair() {

    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
      keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

      System.out.println("public key--->" + Base64.encodeToString(publicKey.getEncoded()));
      System.out.println("private key--->" + Base64.encodeToString(privateKey.getEncoded()));

    } catch (NoSuchAlgorithmException e) {
      log.error("NoSuchAlgorithmException", e);
    }
  }

  public static Key restorePrivateKey(String privateKeyStr) {

    try {
      byte[] keyBytes = Base64.decode(privateKeyStr);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
      return factory.generatePrivate(pkcs8EncodedKeySpec);
    } catch (NoSuchAlgorithmException e) {
      log.error("restore the private key fail!", e);
    } catch (InvalidKeySpecException e) {
      log.error("restore the private key fail!", e);
    }
    return null;
  }

  /**
   * 私钥加密方法
   *
   * @param source 源数据
   * @return
   */
  public static String encryptByPrivateKey(String privateKeyText, String source) {
    Key privateKey = restorePrivateKey(privateKeyText);
    return encrypt(CipherAlgorithm.RSA_ECB_PKCS1, privateKey, source);
  }

  public static String decryptByPublicKey(String publicKeyText, String cryptoSrc) {
    Key publicKey = RsaKeyUtil.restorePublicKey(publicKeyText);
    return decrypt(CipherAlgorithm.RSA_ECB_PKCS1, publicKey, cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param source 源数据
   * @return
   */
  public static String encrypt(CipherAlgorithm algorithm, String publicKeyText, String source) {
    return encrypt(algorithm, RsaKeyUtil.restorePublicKey(publicKeyText), source);
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return
   */
  public static String decrypt(CipherAlgorithm algorithm, String privateKeyText, String cryptoSrc) {
    return decrypt(algorithm, restorePrivateKey(privateKeyText), cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param source 源数据
   * @return
   */
  public static String encrypt(CipherAlgorithm algorithm, Key publicKey, String source) {

    return RsaUtil.encrypt(BC_PROVIDER, algorithm, publicKey, source);
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return
   */
  public static String decrypt(CipherAlgorithm algorithm, Key privateKey, String cryptoSrc) {

    return RsaUtil.decrypt(BC_PROVIDER, algorithm, privateKey, Base64.decode(cryptoSrc));
  }

  public static String sign(SignatureAlgorithm algorithm, PrivateKey privateKey, String content) {
    return Base64.encodeToString(RsaUtil.signBytes(BC_PROVIDER, algorithm, privateKey, content));
  }

  public static boolean verify(
      SignatureAlgorithm algorithm, PublicKey publicKey, String signInBase64, String content) {
    return RsaUtil.verify(BC_PROVIDER, algorithm, publicKey, Base64.decode(signInBase64), content);
  }
}
