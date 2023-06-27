package com.ynthm.common.util;

import com.ynthm.common.enums.security.HmacAlgorithm;
import com.ynthm.common.exception.BaseException;

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
public class HmacHelper {
  private final Mac mac;

  private final String key;

  public String getKey() {
    return key;
  }

  public HmacHelper(String key, HmacAlgorithm algorithm) {
    this.key = key;
    try {
      SecretKey secretKey =
          new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm.getValue());
      mac = Mac.getInstance(secretKey.getAlgorithm());
      mac.init(secretKey);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new BaseException(e);
    }
  }

  public String base64Sign(String content) {
    return Base64.getEncoder().encodeToString(sign(content.getBytes(StandardCharsets.UTF_8)));
  }

  public boolean base64Verify(String signature, String content) {
    return verify(Base64.getDecoder().decode(signature), content.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * @param content .getBytes(StandardCharsets.UTF_8)
   * @return 签名结果
   */
  public byte[] sign(byte[] content) {
    return mac.doFinal(content);
  }

  /**
   * @param signature
   * @param content
   * @return
   */
  public boolean verify(byte[] signature, byte[] content) {
    byte[] result = mac.doFinal(content);
    return Arrays.equals(signature, result);
  }
}
