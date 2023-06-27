package com.ynthm.common.utils;

import com.ynthm.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author ETHAN WANG
 */
@Slf4j
public class SecurityUtil {

  private SecurityUtil() {}

  public static SSLContext sslContext() {
    return sslContext(null, (cert, authType) -> true);
  }

  public static SSLContext sslContext(KeyStore truststore, TrustStrategy trustStrategy) {
    try {
      return SSLContextBuilder.create().loadTrustMaterial(truststore, trustStrategy).build();
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      throw new BaseException(e);
    }
  }

  public static SSLSocketFactory sslSocketFactory() {
    return SecurityUtil.sslContext().getSocketFactory();
  }
}
