package com.ynthm.common.enums.security;

import lombok.Getter;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
public enum SignatureAlgorithm {
  /**
   * SHA1withRSA
   */
  SHA1_RSA("SHA1withRSA"),
  ;

  SignatureAlgorithm(String value) {
    this.value = value;
  }

  private final String value;
}
