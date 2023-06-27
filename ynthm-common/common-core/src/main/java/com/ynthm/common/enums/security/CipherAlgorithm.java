package com.ynthm.common.enums.security;

import lombok.Getter;

/**
 * Sun JDK中，Cipher.getInstance("RSA"); 是 Cipher.getInstance("RSA/ECB/PKCS1Padding", "SunJCE");的简写。
 *
 * <p>注：SunJCE中这个名字“RSA/ECB/PKCS1Padding”取得不好，“RSA/None/PKCS1Padding”可能更合适，因为它并不能自动处理过长的数据，你得提前分割。
 *
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
public enum CipherAlgorithm {
  /** 自 Java 8 起支持 OAEP 1024、2048 */
  RSA_OAEP_1("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
  /** 1024、2048 */
  RSA_OAEP_256("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),
  /** RSAES-PKCS1-v1_5 填充方案 */
  RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),
  AES_ECB("AES/ECB/NoPadding"),
  /** PKCS5Padding */
  AES_CBC("AES/CBC/NoPadding"),
  AES_GCM("AES/GCM/NoPadding"),
  ;

  CipherAlgorithm(String value) {
    this.value = value;
  }

  private final String value;
}
