package com.ynthm.common.util;

import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.SignatureAlgorithm;
import com.ynthm.common.lang.Tuple2;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class RsaUtilTest {

  @Test
  void generatePublicAndPrivate() {
    Tuple2<PublicKey, PrivateKey> tuple2 = RsaKeyUtil.generatePublicAndPrivate();
    String str = "hello";
    String encrypt = RsaUtil.encrypt(CipherAlgorithm.RSA_OAEP_1, tuple2.getT1(), str);
    Assertions.assertEquals(
        str, RsaUtil.decrypt(CipherAlgorithm.RSA_OAEP_1, tuple2.getT2(), encrypt));
  }

  @Test
  void generateKeyPair() {
    Tuple2<String, String> tuple2 = RsaKeyUtil.generatePublicAndPrivateStr();
    String str = "hello";
    String encrypt = RsaUtil.encryptByPrivateKey(tuple2.getT2(), str);
    Assertions.assertEquals(str, RsaUtil.decryptByPublicKey(tuple2.getT1(), encrypt));
  }


  @Test
  void generateKeyPair111() {
    Tuple2<PublicKey, PrivateKey> tuple2 = RsaKeyUtil.generatePublicAndPrivate();
    String str = "hellohellohellohellohe";
    String sign = RsaUtil.sign(SignatureAlgorithm.SHA1_RSA, tuple2.getT2(), str);
    Assertions.assertTrue(RsaUtil.verify(SignatureAlgorithm.SHA1_RSA, tuple2.getT1(), sign, str));
  }
}
