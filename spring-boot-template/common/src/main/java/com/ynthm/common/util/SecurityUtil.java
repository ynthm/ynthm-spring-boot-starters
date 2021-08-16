package com.ynthm.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Objects;

/** @author ETHAN WANG */
public class SecurityUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class);

  /** 指定字符集 */
  private static final String UTF_8 = "UTF-8";

  public static final String PROVIDER = "BC";

  public static final String KEY_ALGORITHM = "RSA";

  public static final String SIGN_TYPE = "SHA1withRSA";

  public static final String PKCS12 = "PKCS12";
  public static final String X509 = "X.509";

  /** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
  public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

  public static final String KEY_ALGORITHM_AES = "AES";

  public static final String CIPHER_ALGORITHM_DEFAULT = "AES/ECB/PKCS5Padding";

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

      System.out.println(
          "public key--->" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
      System.out.println(
          "private key--->" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));

    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("NoSuchAlgorithmException", e);
    }
  }

  public static Key restorePublicKey(String publicKeyStr) {
    PublicKey publicKey = null;
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
      publicKey = factory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("restore the public key fail!", e);
    } catch (InvalidKeySpecException e) {
      LOGGER.error("restore the public key fail!", e);
    }
    return publicKey;
  }

  public static Key restorePrivateKey(String privateKeyStr) {

    try {
      byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
      return factory.generatePrivate(pkcs8EncodedKeySpec);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("restore the private key fail!", e);
    } catch (InvalidKeySpecException e) {
      LOGGER.error("restore the private key fail!", e);
    }
    return null;
  }

  public static PublicKey getPublicKeyFromFile(String certPath) {

    PublicKey publicKey = null;
    try (InputStream certin = new FileInputStream(certPath)) {
      CertificateFactory certificateFactory = CertificateFactory.getInstance(X509, PROVIDER);
      ;
      Certificate certificate = certificateFactory.generateCertificate(certin);
      publicKey = certificate.getPublicKey();
    } catch (CertificateException | NoSuchProviderException | IOException e) {
      LOGGER.error("Get public key failed.", e);
    }

    return publicKey;
  }

  public static PrivateKey getPrivateKeyFromFile(String storePath, String keyPass) {

    PrivateKey privateKey = null;
    try (InputStream in = new FileInputStream(storePath)) {
      KeyStore keyStore = KeyStore.getInstance(PKCS12, PROVIDER);
      ;
      keyStore.load(in, keyPass.toCharArray());

      Enumeration<String> enums = keyStore.aliases();
      while (enums.hasMoreElements()) {
        String keyAlias = enums.nextElement();
        if (keyStore.isKeyEntry(keyAlias)) {
          privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPass.toCharArray());
        }
      }

    } catch (KeyStoreException e) {
      LOGGER.error("Get public key failed.", e);
    } catch (NoSuchProviderException e) {
      LOGGER.error("Get public key failed.", e);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Get public key failed.", e);
    } catch (CertificateException e) {
      LOGGER.error("Get public key failed.", e);
    } catch (IOException e) {
      LOGGER.error("Get public key failed.", e);
    } catch (UnrecoverableKeyException e) {
      LOGGER.error("Get public key failed.", e);
    }

    return privateKey;
  }

  /**
   * 私钥加密方法
   *
   * @param source 源数据
   * @return
   * @throws Exception
   */
  public static String encryptByPrivateKey(String privateKeyText, String source) {
    Key privateKey = restorePrivateKey(privateKeyText);
    return encrypt(privateKey, source);
  }

  public static String decryptByPublicKey(String publicKeyText, String cryptoSrc) {
    Key publicKey = restorePublicKey(publicKeyText);
    return decrypt(publicKey, cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param source 源数据
   * @return
   * @throws Exception
   */
  public static String encrypt(String publicKeyText, String source) {
    Key publicKey = restorePublicKey(publicKeyText);
    return encrypt(publicKey, source);
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return
   * @throws Exception
   */
  public static String decrypt(String privateKeyText, String cryptoSrc) {
    Key privatekey = restorePrivateKey(privateKeyText);
    return decrypt(privatekey, cryptoSrc);
  }

  /**
   * 公钥加密方法 分段加密
   *
   * @param source 源数据
   * @return
   * @throws Exception
   */
  public static String encrypt(Key publicKey, String source) {
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] data = source.getBytes(UTF_8);

      /** 执行分组加密操作 */
      byte[] encryptedData = segment(cipher, data, MAX_ENCRYPT_BLOCK);
      return Base64.getEncoder().encodeToString(encryptedData);
    } catch (Exception e) {
      LOGGER.error("encrypt fail!", e);
    }

    return "";
  }

  /**
   * 私钥解密算法 分段加密
   *
   * @param cryptoSrc 密文
   * @return
   * @throws Exception
   */
  public static String decrypt(Key privateKey, String cryptoSrc) {

    byte[] decryptedData = null;
    String result = null;
    try {
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);

      byte[] encryptedData = Base64.getDecoder().decode(cryptoSrc);
      decryptedData = segment(cipher, encryptedData, MAX_DECRYPT_BLOCK);
      result = new String(decryptedData, UTF_8);

    } catch (Exception e) {
      LOGGER.error("decrypt fail!", e);
    }

    return result;
  }

  private static byte[] segment(Cipher cipher, byte[] bytes, int maxBlock)
      throws IOException, IllegalBlockSizeException, BadPaddingException {

    byte[] decryptedData;
    /** 执行解密操作 */
    int inputLen = bytes.length;

    int offSet = 0;
    byte[] cache;
    int i = 0;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
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
    }

    return decryptedData;
  }

  public static String sign(PrivateKey privateKey, String content) {
    Signature signature;
    byte[] signByte = null;

    try {
      signature = Signature.getInstance(SIGN_TYPE);
      signature.initSign(privateKey);
      signature.update(content.getBytes(UTF_8));
      signByte = signature.sign();
    } catch (NoSuchAlgorithmException e) {

      LOGGER.error("sign failed.", e);
    } catch (InvalidKeyException e) {

      LOGGER.error("sign failed.", e);
    } catch (SignatureException e) {

      LOGGER.error("sign failed.", e);
    } catch (UnsupportedEncodingException e) {

      LOGGER.error("sign failed.", e);
    }

    return Base64.getEncoder().encodeToString(signByte);
  }

  public static boolean verify(PublicKey publicKey, String sign, String content) {
    Signature signature;
    boolean verified = false;
    try {
      signature = Signature.getInstance(SIGN_TYPE);
      signature.initVerify(publicKey);
      signature.update(content.getBytes(UTF_8));
      verified = signature.verify(Base64.getDecoder().decode(sign));
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("verify failed.", e);
    } catch (InvalidKeyException e) {
      LOGGER.error("verify failed.", e);
    } catch (SignatureException e) {
      LOGGER.error("verify failed.", e);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("verify failed.", e);
    }

    return verified;
  }

  /**
   * 密码生成加密秘钥 作为加密和解密密钥且只有密钥相同解密加密才会成功
   *
   * @return
   */
  public static Key getAesKey(final String key) {
    Objects.requireNonNull(key);
    try {
      // 构造密钥生成器 指定为AES算法 不区分大小写
      KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM_AES);
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
      random.setSeed(key.getBytes());

      // AES 要求密钥长度为 128
      kg.init(128, random);

      // 产生原始对称密钥
      SecretKey secretKey = kg.generateKey();

      // key转换,根据字节数组生成AES密钥
      return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM_AES);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("no such algorithm exception.", e);
    } catch (NoSuchProviderException e) {
      LOGGER.error("no such provider exception.", e);
    }

    return null;
  }

  /**
   * 加密
   *
   * @param context 需要加密的明文
   * @param key 加密用密钥
   * @return
   */
  public static String aesEncrypt(String context, String key) {
    byte[] bytes = aesEncrypt(context, getAesKey(key));
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * 加密
   *
   * @param context 需要加密的明文
   * @param key 加密用密钥
   * @return
   */
  public static byte[] aesEncrypt(String context, Key key) {
    try {

      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_DEFAULT);
      cipher.init(cipher.ENCRYPT_MODE, key);
      // 将加密并编码后的内容解码成字节数组
      byte[] result = cipher.doFinal(context.getBytes(UTF_8));

      return result;

    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | IllegalBlockSizeException
        | BadPaddingException
        | UnsupportedEncodingException e) {
      LOGGER.error("AES encrypt.", e);
      return null;
    }
  }

  /**
   * 解密
   *
   * @param result 加密后的密文byte数组
   * @param key 解密用密钥
   */
  public static String aesDecrypt(String result, String key) {
    try {
      byte[] bytes = aesDecrypt(Base64.getDecoder().decode(result), getAesKey(key));

      return new String(bytes, UTF_8);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("decrypt fail!", e);
    }

    return "";
  }

  /**
   * 解密
   *
   * @param result 加密后的密文byte数组
   * @param key 解密用密钥
   */
  public static byte[] aesDecrypt(byte[] result, Key key) {

    Cipher cipher;
    try {

      cipher = Cipher.getInstance(CIPHER_ALGORITHM_DEFAULT);
      // 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
      cipher.init(Cipher.DECRYPT_MODE, key);
      return cipher.doFinal(result);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }

    return new byte[] {};
  }
}
