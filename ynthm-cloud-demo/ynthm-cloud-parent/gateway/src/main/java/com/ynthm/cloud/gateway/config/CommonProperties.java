package com.ynthm.cloud.gateway.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ynthm")
public class CommonProperties {
  private List<String> whitelistUrls;
  private JwtProperties jwt;

  @Data
  static class JwtProperties {
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
  }
}
