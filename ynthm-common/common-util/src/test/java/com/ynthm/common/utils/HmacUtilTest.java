package com.ynthm.common.utils;

import com.google.common.base.MoreObjects;
import com.ynthm.common.enums.security.HmacAlgorithm;
import com.ynthm.common.util.HmacHelper;
import com.ynthm.common.util.HmacUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Ethan Wang
 */
class HmacUtilTest {

  @Test
  void testGuava() {
    System.out.println(
        MoreObjects.toStringHelper("error")
            .add("url", "https://www.ynthm.com")
            .add("message", "404")
            .toString());
  }

  @Test
  void getHmacKey() {

    System.out.println(HmacUtil.hexHmacKey(HmacAlgorithm.HMAC_MD5));
    System.out.println(
        new String(HmacUtil.hmacKey(HmacAlgorithm.HMAC_MD5), StandardCharsets.UTF_8));
    System.out.println(
        Base64.getEncoder().encodeToString(HmacUtil.hmacKey(HmacAlgorithm.HMAC_MD5)));

    System.out.println("--------------------");
    System.out.println(HmacUtil.hexHmacKey(HmacAlgorithm.HMAC_SHA256));
    System.out.println(
        new String(HmacUtil.hmacKey(HmacAlgorithm.HMAC_SHA256), StandardCharsets.UTF_8));
    System.out.println(
        Base64.getEncoder().encodeToString(HmacUtil.hmacKey(HmacAlgorithm.HMAC_SHA256)));

    System.out.println("--------------------");
    System.out.println(HmacUtil.hexHmacKey(HmacAlgorithm.HMAC_SHA512));
    System.out.println(
        new String(HmacUtil.hmacKey(HmacAlgorithm.HMAC_SHA512), StandardCharsets.UTF_8));
    System.out.println(
        Base64.getEncoder().encodeToString(HmacUtil.hmacKey(HmacAlgorithm.HMAC_SHA512)));
  }

  @Test
  void testHmacHelper() {
    String key = "8361c232a80ff8989aef55bd7758e1b52ea791caeb4b5dd84c110d13a659d3f8";
    String data = "我爱中华人民共和国";
    HmacHelper helper = new HmacHelper(key, HmacAlgorithm.HMAC_SHA256);
    byte[] sign = helper.sign(data.getBytes(StandardCharsets.UTF_8));
    System.out.println(helper.verify(sign, data.getBytes(StandardCharsets.UTF_8)));

    String sign1 = helper.base64Sign(data);
    System.out.println(helper.base64Verify(sign1, data));
  }

  @Test
  void encryptHmac() {

    String key = "8361c232a80ff8989aef55bd7758e1b52ea791caeb4b5dd84c110d13a659d3f8";
    String data = "我爱中华人民共和国";
    String encrypt = HmacUtil.base64Encrypt(HmacAlgorithm.HMAC_SHA256, key, data);

    System.out.println(HmacUtil.base64Verify(HmacAlgorithm.HMAC_SHA256, key, data, encrypt));
    System.out.println(
        HmacUtil.verify(
            HmacAlgorithm.HMAC_SHA256,
            key.getBytes(StandardCharsets.UTF_8),
            data.getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(encrypt)));
  }
}
