package com.ynthm.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class SecureHashUtilTest {

  @Test
  void test(){
    System.out.println(SecureHashUtil.md5("653ca0b5-6b48-48c0-8254-7cb9c427041a"));
    System.out.println(SecureHashUtil.md5("e6c1bf1f-9709-49ed-bd5b-1e220ed2104c"));
    System.out.println(SecureHashUtil.md5("075befe1a2a3ef9a"));
  }

}