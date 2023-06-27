package com.ynthm.common.constant;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.security.spec.PSSParameterSpec;
import java.util.Random;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
class PssParamsHolderTest {

  @Test
  void testAll() {

    SecureRandom NOT_SECURE_RANDOM =
        new SecureRandom() {
          Random r = new Random();

          @Override
          public void nextBytes(byte[] bytes) {
            r.nextBytes(bytes);
          }
        };

    byte[] msg = "hello".getBytes();

    matrix(PssParamsHolder.PSS_1_SPEC);
    matrix(PssParamsHolder.PSS_256_SPEC);
    matrix(PssParamsHolder.PSS_384_SPEC);
    matrix(PssParamsHolder.PSS_512_SPEC);
  }

  void matrix(PSSParameterSpec pss) {

    System.out.printf(
        "\n%10s%20s%20s%20s  %s\n",
        pss.getDigestAlgorithm(), "KeyPairGenerator", "signer", "verifier", "result");
    System.out.printf(
        "%10s%20s%20s%20s  %s\n", "-------", "----------------", "------", "--------", "------");

    // KeyPairGenerator chooses SPI when getInstance() is called.
    String[] provsForKPG = {"SunRsaSign"};

    // "-" means no preferred provider. In this case, SPI is chosen
    // when initSign/initVerify is called. Worth testing.
    String[] provsForSignature = {"SunRsaSign", "-"};

    int pos = 0;
    for (String pg : provsForKPG) {
      for (String ps : provsForSignature) {
        for (String pv : provsForSignature) {
          System.out.printf("%10d%20s%20s%20s  ", ++pos, pg, ps, pv);
          try {
            boolean result = test(pg, ps, pv, pss);
            System.out.println(result);
          } catch (Exception e) {
            if (pg.equals("-") || pg.equals(ps)) {
              // When Signature provider is automatically
              // chosen or the same with KeyPairGenerator,
              // this is an error.
              // allResult = false;
              System.out.println("X " + e.getMessage());
            } else {
              // Known restriction: SunRsaSign and SunMSCAPI can't
              // use each other's private key for signing.
              System.out.println(e.getMessage());
            }
          }
        }
      }
    }
  }

  static boolean test(String pg, String ps, String pv, PSSParameterSpec pss) throws Exception {
    byte[] msg = "hello".getBytes();
    KeyPairGenerator kpg =
        pg.length() == 1
            ? KeyPairGenerator.getInstance("RSA")
            : KeyPairGenerator.getInstance("RSA", pg);
    kpg.initialize(pss.getDigestAlgorithm().equals("SHA-512") ? 2048 : 1024, new SecureRandom());
    KeyPair kp = kpg.generateKeyPair();
    PrivateKey pr = kp.getPrivate();
    PublicKey pu = kp.getPublic();

    Signature s =
        ps.length() == 1
            ? Signature.getInstance("RSASSA-PSS")
            : Signature.getInstance("RSASSA-PSS", ps);
    s.initSign(pr);
    s.setParameter(pss);
    s.update(msg);
    byte[] sig = s.sign();

    Signature s2 =
        pv.length() == 1
            ? Signature.getInstance("RSASSA-PSS")
            : Signature.getInstance("RSASSA-PSS", pv);
    s2.initVerify(pu);
    s2.setParameter(pss);
    s2.update(msg);

    return s2.verify(sig);
  }
}
