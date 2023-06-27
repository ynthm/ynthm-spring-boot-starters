package com.ynthm.common.enums.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ethan Wang
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum HmacAlgorithm {
  /** HmacMD5 摘要长度 128 */
  HMAC_MD5("HmacMD5"),
  HMAC_SHA1("HmacSHA1"),
  HMAC_SHA256("HmacSHA256"),
  HMAC_SHA384("HmacSHA384"),
  HMAC_SHA512("HmacSHA512");

  private final String value;
}
