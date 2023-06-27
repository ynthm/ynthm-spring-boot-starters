package com.ynthm.common.util;

import com.ynthm.common.constant.Constant;

import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author ynthm wang
 */
public class UUID5 {
  private UUID5() {}

  public static UUID from(String name) {
    return fromBytes(name.getBytes(Constant.CHARSET_UTF_8));
  }

  public static UUID fromBytes(byte[] name) {
    if (name == null) {
      throw new NullPointerException("name == null");
    }
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      return make(md.digest(name), 5);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static UUID make(byte[] hash, int version) {
    long msb = peekLong(hash, 0, ByteOrder.BIG_ENDIAN);
    long lsb = peekLong(hash, 8, ByteOrder.BIG_ENDIAN);
    // Set the version field
    msb &= ~(0xfL << 12);
    msb |= ((long) version) << 12;
    // Set the variant field to 2
    lsb &= ~(0x3L << 62);
    lsb |= 2L << 62;
    return new UUID(msb, lsb);
  }

  private static long peekLong(final byte[] src, final int offset, final ByteOrder order) {
    long ans = 0;
    if (order == ByteOrder.BIG_ENDIAN) {
      for (int i = offset; i < offset + 8; i += 1) {
        ans <<= 8;
        ans |= src[i] & 0xffL;
      }
    } else {
      for (int i = offset + 7; i >= offset; i -= 1) {
        ans <<= 8;
        ans |= src[i] & 0xffL;
      }
    }
    return ans;
  }
}
