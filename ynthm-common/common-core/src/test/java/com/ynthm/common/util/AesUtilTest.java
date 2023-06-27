package com.ynthm.common.util;

import com.ynthm.common.constant.Constant;
import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.KeyBitLength;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class AesUtilTest {

  @Test
  void cbcTest() {
    byte[] aesKey = AesUtil.generateAesKey(SecureHashUtil.salt(), KeyBitLength.TWENTY_FIVE);
    byte[] iv = AesUtil.iv();
    byte[] originalBytes = "hellohellohellol".getBytes(Constant.CHARSET_UTF_8);
    byte[] data = AesUtil.encryptCbc(CipherAlgorithm.AES_CBC, aesKey, iv, originalBytes);
    byte[] bytes = AesUtil.decryptCbc(CipherAlgorithm.AES_CBC, aesKey, iv, data);

    Assertions.assertArrayEquals(originalBytes, bytes);
  }

  @Test
  void ecb() {
    byte[] aesKey = AesUtil.generateAesKey(SecureHashUtil.salt(), KeyBitLength.TWENTY_FIVE);
    byte[] iv = AesUtil.iv();
    byte[] originalBytes = "hellohellohellol".getBytes(Constant.CHARSET_UTF_8);
    byte[] data = AesUtil.encryptEcb(CipherAlgorithm.AES_ECB, aesKey, originalBytes);
    byte[] bytes = AesUtil.decryptEcb(CipherAlgorithm.AES_ECB, aesKey, data);

    Assertions.assertArrayEquals(originalBytes, bytes);
  }

  @Test
  void gcm() {
    byte[] aesKey = AesUtil.generateAesKey(SecureHashUtil.salt(), KeyBitLength.TWENTY_FIVE);
    byte[] iv = AesUtil.iv();
    byte[] aad = AesUtil.iv();
    byte[] originalBytes = "hello".getBytes(Constant.CHARSET_UTF_8);
    byte[] data = AesUtil.encryptGcm(CipherAlgorithm.AES_GCM, aesKey, iv, aad, originalBytes);
    byte[] bytes = AesUtil.decryptGcm(CipherAlgorithm.AES_GCM, aesKey, iv, aad, data);

    Assertions.assertArrayEquals(originalBytes, bytes);
  }
}
