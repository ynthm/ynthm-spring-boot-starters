package com.ynthm.common.util;

import com.ynthm.common.constant.SecurityConst;
import com.ynthm.common.enums.security.KeyStoreType;
import com.ynthm.common.lang.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
class RsaKeyUtilTest {

  @Test
  void securityServiceAlgorithms() {
    log.info(
        "default algorithm: keystore {} truststore {}",
        KeyManagerFactory.getDefaultAlgorithm(),
        TrustManagerFactory.getDefaultAlgorithm());
    Set<String> keyStoreAlgorithms = Security.getAlgorithms("KeyStore");
    Assertions.assertTrue(keyStoreAlgorithms.contains(KeyStoreType.PKCS12.getValue()));
    log.info(
        "Provider {}",
        Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList()));

    Set<String> serviceNames =
        Arrays.stream(Security.getProviders())
            .map(Provider::getServices)
            .flatMap(Collection::stream)
            .map(Provider.Service::getType)
            .collect(Collectors.toSet());
    log.info("security services {}", serviceNames);
    for (String serviceName : serviceNames) {
      log.info("{} {}", serviceName, Security.getAlgorithms(serviceName));
    }
  }

  @Test
  void getCertificate() throws KeyStoreException, URISyntaxException, CertificateEncodingException {
    X509Certificate certificate =
        RsaKeyUtil.getCertificate(ResourceUtil.getResourceAsStream("selfsignedcert.cer"));
    log.info(
        "SHA256withRSA {} CertificateFactory {}",
        certificate.getSigAlgName(),
        certificate.getType());
    Assertions.assertEquals(SecurityConst.X509, certificate.getType());

    String alias = "trust1";

    KeyStore trustStore =
        RsaKeyUtil.keyStoreFromCert(
            KeyStoreType.PKCS12,
            alias,
            ResourceUtil.getResourcePath("selfsignedcert.cer"),
            "changeit");
    Certificate certificate1 = trustStore.getCertificate(alias);
    Assertions.assertArrayEquals(certificate.getEncoded(), certificate1.getEncoded());
  }

  @Test
  void restorePublicKey() {
    Tuple2<String, String> tuple2 = RsaKeyUtil.generatePublicAndPrivateStr();
    Key publicKey = RsaKeyUtil.restorePublicKey(tuple2.getT1());
    Key privateKey = RsaKeyUtil.restorePrivateKey(tuple2.getT2());
    Assertions.assertNotNull(publicKey);
    Assertions.assertNotNull(privateKey);
  }

  @Test
  void getPublicKey() {

    String pubKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwGNQnBgetrtteKd8/kYPg9neRoDsjuEi7YJmPMOt8h+kK"
            + "/wnvABvvZAxX4vgU7M0vgxr16mfZBWv3fqPxn4+eKAhN/jt1cjKal7PyBA8Qxz22awGaJTYKNvFIpceb1l6NaQiaLt0Z1Oq/lP2hLKfEk3c3Nw5lW64WpZjYfWSRB4z7z5Ju44fvutNO6pyPEL0qovRKJPuitkeqO7vnih3O2EN6WEJCi9EC7E8ghyPWOb52LVDR2WSaezPIcc64lsbGPX0pLWRfRk9XwpWEjiyovUk/8spjJoOB/M8lktG30sRR1rEQMNoC3DaJCwSFbjvztJO8odVwn4JI86MzNmxLwIDAQAB";
    Assertions.assertEquals(
        pubKey,
        Base64.getEncoder()
            .encodeToString(
                RsaKeyUtil.getPublicKeyFromCer(
                        ResourceUtil.getResourceAsStream("selfsignedcert.cer"))
                    .getEncoded()));

    Key key = RsaKeyUtil.restorePublicKey(pubKey);
    System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
  }

  @Test
  void getFromKeyStore() {
    String pubKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwGNQnBgetrtteKd8/kYPg9neRoDsjuEi7YJmPMOt8h+kK"
            + "/wnvABvvZAxX4vgU7M0vgxr16mfZBWv3fqPxn4+eKAhN/jt1cjKal7PyBA8Qxz22awGaJTYKNvFIpceb1l6NaQiaLt0Z1Oq/lP2hLKfEk3c3Nw5lW64WpZjYfWSRB4z7z5Ju44fvutNO6pyPEL0qovRKJPuitkeqO7vnih3O2EN6WEJCi9EC7E8ghyPWOb52LVDR2WSaezPIcc64lsbGPX0pLWRfRk9XwpWEjiyovUk/8spjJoOB/M8lktG30sRR1rEQMNoC3DaJCwSFbjvztJO8odVwn4JI86MzNmxLwIDAQAB";

    Tuple2<PublicKey, PrivateKey> fromKeyStore =
        RsaKeyUtil.getFromKeyStore(
            KeyStoreType.JKS,
            ResourceUtil.getResourceAsStream("keystore.jks"),
            "jks-keystore",
            "changeit",
            "changeit");

    PublicKey publicKey = fromKeyStore.getT1();

    Assertions.assertEquals(pubKey, Base64.getEncoder().encodeToString(publicKey.getEncoded()));
    PrivateKey privateKey = fromKeyStore.getT2();
    System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
  }

  @Test
  void getTrustStore() {

    String pubKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwGNQnBgetrtteKd8/kYPg9neRoDsjuEi7YJmPMOt8h+kK"
            + "/wnvABvvZAxX4vgU7M0vgxr16mfZBWv3fqPxn4+eKAhN/jt1cjKal7PyBA8Qxz22awGaJTYKNvFIpceb1l6NaQiaLt0Z1Oq/lP2hLKfEk3c3Nw5lW64WpZjYfWSRB4z7z5Ju44fvutNO6pyPEL0qovRKJPuitkeqO7vnih3O2EN6WEJCi9EC7E8ghyPWOb52LVDR2WSaezPIcc64lsbGPX0pLWRfRk9XwpWEjiyovUk/8spjJoOB/M8lktG30sRR1rEQMNoC3DaJCwSFbjvztJO8odVwn4JI86MzNmxLwIDAQAB";
    Assertions.assertEquals(
        pubKey,
        Base64.getEncoder()
            .encodeToString(
                RsaKeyUtil.getPublicKeyFromTrustStore(
                        KeyStoreType.JKS,
                        ResourceUtil.getResourceAsStream("truststore.jks"),
                        "jks-truststore",
                        "changeit")
                    .getEncoded()));
  }
}
