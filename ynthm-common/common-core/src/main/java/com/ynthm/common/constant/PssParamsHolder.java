package com.ynthm.common.constant;

import sun.security.util.KeyUtil;

import java.security.Key;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class PssParamsHolder {

  private PssParamsHolder() {}

  public static final PSSParameterSpec PSS_1_SPEC = PSSParameterSpec.DEFAULT;

  public static final PSSParameterSpec PSS_256_SPEC =
      new PSSParameterSpec(
          "SHA-256",
          "MGF1",
          new MGF1ParameterSpec("SHA-256"),
          32,
          PSSParameterSpec.TRAILER_FIELD_BC);
  public static final PSSParameterSpec PSS_384_SPEC =
      new PSSParameterSpec(
          "SHA-384",
          "MGF1",
          new MGF1ParameterSpec("SHA-384"),
          48,
          PSSParameterSpec.TRAILER_FIELD_BC);
  public static final PSSParameterSpec PSS_512_SPEC =
      new PSSParameterSpec(
          "SHA-512",
          "MGF1",
          new MGF1ParameterSpec("SHA-512"),
          64,
          PSSParameterSpec.TRAILER_FIELD_BC);

  /**
   * AlgorithmId.getDefaultAlgorithmParameterSpec(sigAlgName, privateKey);
   *
   * @param k 秘钥
   * @return 结果
   */
  public static PSSParameterSpec pssParameterSpec(Key k) {
    switch (ifcFfcStrength(KeyUtil.getKeySize(k))) {
      case "SHA256":
        return PSS_256_SPEC;
      case "SHA384":
        return PSS_384_SPEC;
      case "SHA512":
        return PSS_512_SPEC;
      default:
        throw new AssertionError("Should not happen");
    }
  }

  private static String ifcFfcStrength(int bitLength) {
    // 256 bits
    if (bitLength > 7680) {
      return "SHA512";
      // 192 bits
    } else if (bitLength > 3072) {
      return "SHA384";
    } else { // 128 bits and less
      return "SHA256";
    }
  }
}
