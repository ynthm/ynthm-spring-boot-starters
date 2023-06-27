package com.ynthm.common.utils;

import com.ynthm.common.enums.security.KeyStoreType;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
class RsaBcUtilTest {

  @Test
  void generateKeyPair() {
    BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
    Security.addProvider(bouncyCastleProvider);

    log.info(
        "{}",
        Arrays.stream(Security.getProviders()).map(Provider::getName).collect(Collectors.toList()));
    securityServiceAlgorithms(bouncyCastleProvider);
    String subStr = "CBC";
    log.info(
        "algorithm contains {}:{}",
        subStr,
        algorithms(bouncyCastleProvider, serviceNames(bouncyCastleProvider), subStr));
  }

  void securityServiceAlgorithms(Provider provider) {

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
        provider.getServices().stream().map(Provider.Service::getType).collect(Collectors.toSet());
    log.info("security services {}", serviceNames);
    for (String serviceName : serviceNames) {
      log.info("{} {}", serviceName, Security.getAlgorithms(serviceName));
    }
  }

  private Set<String> algorithms(
      Provider provider, Stream<String> serviceNames, CharSequence contains) {
    return serviceNames
        .map(Security::getAlgorithms)
        .flatMap(Collection::stream)
        .filter(algorithm -> algorithm.contains(contains))
        .collect(Collectors.toSet());
  }

  private Stream<String> serviceNames(Provider provider) {
    return provider.getServices().stream().map(Provider.Service::getType);
  }

  @Test
  void restorePrivateKey() {}

  @Test
  void encryptByPrivateKey() {}
}
