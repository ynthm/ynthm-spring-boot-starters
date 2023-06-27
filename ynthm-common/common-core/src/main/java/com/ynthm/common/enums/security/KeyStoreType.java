package com.ynthm.common.enums.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public
enum KeyStoreType {
  /**
   * JKS
   */
  JKS("JKS"),
  PKCS12("PKCS12"),
  ;

  private final String value;
}
