package com.ynthm.common.enums.security;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public enum KeyBitLength {
  /**
   * 基本长度
   */
  SIXTEEN(128),
  TWENTY_FIVE(192),
  THIRTY_TWO(256);

  KeyBitLength(int length) {
    this.length = length;
  }

  private final int length;

  public int getLength() {
    return length;
  }
}
