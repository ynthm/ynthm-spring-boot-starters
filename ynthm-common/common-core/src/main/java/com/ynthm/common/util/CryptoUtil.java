package com.ynthm.common.util;

import com.google.common.base.Strings;
import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.exception.UtilException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class CryptoUtil {

  private CryptoUtil() {}

  public static Cipher getCipher(
      String provider,
      CipherAlgorithm algorithm,
      int opMode,
      Key key,
      AlgorithmParameterSpec params) {

    try {
      Cipher cipher = getCipher(provider, algorithm);

      cipher.init(opMode, key, params);
      return cipher;
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | NoSuchProviderException
        | InvalidKeyException
        | InvalidAlgorithmParameterException e) {
      throw new UtilException(e);
    }
  }

  private static Cipher getCipher(String provider, CipherAlgorithm algorithm)
      throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
    Cipher cipher;
    String algorithmValue = algorithm.getValue();
    if (Strings.isNullOrEmpty(provider)) {
      cipher = Cipher.getInstance(algorithmValue);
    } else {
      cipher = Cipher.getInstance(algorithmValue, provider);
    }
    return cipher;
  }

  public static Cipher getCipher(String provider, CipherAlgorithm algorithm, int opMode, Key key) {

    try {
      Cipher cipher = getCipher(provider, algorithm);

      cipher.init(opMode, key);
      return cipher;
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | NoSuchProviderException
        | InvalidKeyException e) {
      throw new UtilException(e);
    }
  }

  public static Cipher getAesDecryptCbc(
      String provider, CipherAlgorithm transformation, int opMode, byte[] key, byte[] iv) {
    return getCipher(
        provider, transformation, opMode, AesUtil.secretKeySpec(key), new IvParameterSpec(iv));
  }

  public static Cipher getAesDecryptGcm(
      String provider, CipherAlgorithm transformation, int opMode, byte[] key, byte[] iv) {
    return getCipher(
        provider,
        transformation,
        opMode,
        AesUtil.secretKeySpec(key),
        new GCMParameterSpec(128, iv));
  }
}
