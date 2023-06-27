package com.ynthm.common.enums.security;

import lombok.Getter;

/**
 * jdk7 默认是 TLSv1，jdk8 默认是 TLSv1.2
 *
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
public enum Tls {
  /** DEFAULT */
  DEFAULT("DEFAULT"),
  TLS("TLS"),
  TLS_1("TLSv1"),
  TLS_1_1("TLSv1.1"),
  TLS_1_2("TLSv1.2"),
  TLS_1_3("TLSv1.3");

  Tls(String value) {
    this.value = value;
  }

  private final String value;
}
