package com.ynthm.common.util;

import com.ynthm.common.constant.Constant;
import com.ynthm.common.constant.PssParamsHolder;
import com.ynthm.common.enums.security.CipherAlgorithm;
import com.ynthm.common.enums.security.SignatureAlgorithm;
import com.ynthm.common.lang.Tuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PSSParameterSpec;

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
  void testPss() {
    Tuple2<PublicKey, PrivateKey> tuple2 = RsaKeyUtil.generatePublicAndPrivate();
    PSSParameterSpec pssParameterSpec = PssParamsHolder.pssParameterSpec(tuple2.getT1());
    byte[] content = "你好".getBytes(Constant.CHARSET_UTF_8);
    byte[] sign = RsaUtil.signPss(tuple2.getT2(), pssParameterSpec, content);

    Assertions.assertTrue(RsaUtil.verifyPss(tuple2.getT1(), pssParameterSpec, sign, content));
  }

  @Test
  void generateKeyPair111() {
    Tuple2<PublicKey, PrivateKey> tuple2 = RsaKeyUtil.generatePublicAndPrivate();
    String str = "hellohellohellohellohe";
    String sign = RsaUtil.sign(SignatureAlgorithm.SHA1_RSA, tuple2.getT2(), str);
    Assertions.assertTrue(RsaUtil.verify(SignatureAlgorithm.SHA1_RSA, tuple2.getT1(), sign, str));
  }
}
