package com.ynthm.common.util;

import com.ynthm.common.constant.SecurityConst;
import com.ynthm.common.enums.security.KeyStoreType;
import com.ynthm.common.enums.security.Tls;
import com.ynthm.common.exception.UtilException;
import com.ynthm.common.lang.Tuple2;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.Objects;

/**
 * 从证书库 keystore 文件提取公私钥，及证书 cer 文件中提取公钥
 *
 * @author Ynthm Wang
 * @version 1.0
 */
public class RsaKeyUtil {

  /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
  public static final int KEY_SIZE = 1024;

  private RsaKeyUtil() {}

  /**
   * 生成密钥对。注意这里是生成密钥对KeyPair，再由密钥对获取公私钥
   *
   * @return 公钥及私钥
   */
  public static Tuple2<String, String> generatePublicAndPrivateStr() {

    Tuple2<PublicKey, PrivateKey> tuple2 = generatePublicAndPrivate();
    return Tuple2.of(
        Base64.encodeToString(tuple2.getT1().getEncoded()),
        Base64.encodeToString(tuple2.getT2().getEncoded()));
  }

  public static Tuple2<PublicKey, PrivateKey> generatePublicAndPrivate() {
    return generatePublicAndPrivate(KEY_SIZE);
  }

  public static Tuple2<PublicKey, PrivateKey> generatePublicAndPrivate(int keySize) {

    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SecurityConst.KEY_ALGORITHM);
      keyPairGenerator.initialize(keySize, new SecureRandom());
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

      return Tuple2.of(publicKey, privateKey);
    } catch (NoSuchAlgorithmException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 获取公钥
   *
   * @param publicKey KeyPairGenerator 产生的
   * @return 公钥
   */
  public static Key restorePublicKey(String publicKey) {
    try {
      byte[] keyBytes = Base64.decode(publicKey);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory factory = KeyFactory.getInstance(SecurityConst.KEY_ALGORITHM);
      return factory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 获取私钥
   *
   * @param privateKey KeyPairGenerator 产生的
   * @return 私钥
   */
  public static Key restorePrivateKey(String privateKey) {

    try {
      byte[] keyBytes = Base64.decode(privateKey);
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory factory = KeyFactory.getInstance(SecurityConst.KEY_ALGORITHM);
      return factory.generatePrivate(pkcs8EncodedKeySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 从 .cer 证书导出公钥
   *
   * @param certPath 路径
   * @return 公钥
   */
  public static PublicKey getPublicKeyFromCer(String certPath) {

    try {
      return getPublicKeyFromCer(Files.newInputStream(Paths.get(certPath)));
    } catch (IOException e) {
      throw new UtilException(e);
    }
  }

  public static PublicKey getPublicKeyFromCer(InputStream inputStream) {
    return getCertificate(inputStream).getPublicKey();
  }

  public static X509Certificate getCertificate(InputStream inputStream) {
    try (InputStream is = inputStream) {
      CertificateFactory certificatefactory = CertificateFactory.getInstance(SecurityConst.X509);
      return (X509Certificate) certificatefactory.generateCertificate(is);
    } catch (IOException | CertificateException e) {
      throw new UtilException(e);
    }
  }

  public final Collection<X509Certificate> generateCertificates(final String trustedCertificates) {
    try (InputStream is =
        new ByteArrayInputStream(trustedCertificates.getBytes(StandardCharsets.US_ASCII))) {
      return CastUtil.cast(
          CertificateFactory.getInstance(SecurityConst.X509).generateCertificates(is));
    } catch (IOException | CertificateException e) {
      throw new UtilException(e);
    }
  }

  public static Tuple2<PublicKey, PrivateKey> getFromKeyStore(
      KeyStoreType type, String storePath, String alias, String storePass, String keyPass) {

    try {
      return getFromKeyStore(
          type, Files.newInputStream(Paths.get(storePath)), alias, storePass, keyPass);
    } catch (IOException e) {
      throw new UtilException(e);
    }
  }

  public static Tuple2<PublicKey, PrivateKey> getFromKeyStore(
      KeyStoreType type,
      InputStream keyStoreInputStream,
      String alias,
      String storePass,
      String keyPass) {
    try {
      KeyStore ks = getKeyStore(type, keyStoreInputStream, storePass);
      return Tuple2.of(
          ks.getCertificate(alias).getPublicKey(),
          (PrivateKey) ks.getKey(alias, keyPass.toCharArray()));

    } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
      throw new UtilException(e);
    }
  }

  public static PublicKey getPublicKeyFromTrustStore(
      KeyStoreType type, InputStream trustStoreInputStream, String alias, String trustStorePass) {

    try {
      return getKeyStore(type, trustStoreInputStream, trustStorePass)
          .getCertificate(alias)
          .getPublicKey();
    } catch (KeyStoreException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 获取 KeyStore 或 TrustStore
   *
   * @param type KeyStoreType
   * @param storeFileIs keystore 文件输入流
   * @param storePass keystore password
   * @return KeyStore
   */
  public static KeyStore getKeyStore(KeyStoreType type, InputStream storeFileIs, String storePass) {
    try (InputStream is = storeFileIs) {
      String keyStoreType = type.getValue();
      if ((Objects.isNull(keyStoreType))) {
        keyStoreType = KeyStore.getDefaultType();
      }

      KeyStore trustStore = KeyStore.getInstance(keyStoreType);
      trustStore.load(is, storePass.toCharArray());
      return trustStore;
    } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
      throw new UtilException(e);
    }
  }

  public static SSLContext createSslContext(
      KeyStoreType type,
      InputStream keyStoreIs,
      String storePass,
      InputStream trustStoreIs,
      String trustStorePass) {
    try {
      KeyManager[] km = null;
      if (Objects.nonNull(keyStoreIs)) {
        KeyStore keyStore = getKeyStore(type, keyStoreIs, storePass);
        // 创建密钥管理器
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SecurityConst.X509);
        keyManagerFactory.init(keyStore, storePass.toCharArray());
        km = keyManagerFactory.getKeyManagers();
      }

      TrustManager[] tm = null;
      if (Objects.nonNull(trustStoreIs)) {
        KeyStore trustStore = getKeyStore(type, trustStoreIs, trustStorePass);
        // 创建信任库
        TrustManagerFactory trustManagerFactory =
            TrustManagerFactory.getInstance(SecurityConst.X509);
        trustManagerFactory.init(trustStore);
        tm = trustManagerFactory.getTrustManagers();
      }

      // 初始化 SSLContext
      SSLContext sslContext = SSLContext.getInstance(Tls.TLS_1_3.getValue());
      sslContext.init(km, tm, new SecureRandom());

      return sslContext;
    } catch (Exception ex) {
      throw new UtilException(ex);
    }
  }

  public static KeyStore keyStoreFromCert(
      KeyStoreType type, String alias, final Path certPath, String storePass) {
    try {
      KeyStore trustStore = KeyStore.getInstance(type.getValue());
      trustStore.load(null, storePass.toCharArray());
      trustStore.setCertificateEntry(alias, getCertificate(Files.newInputStream(certPath)));
      return trustStore;
    } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
      throw new UtilException(e);
    }
  }
}
