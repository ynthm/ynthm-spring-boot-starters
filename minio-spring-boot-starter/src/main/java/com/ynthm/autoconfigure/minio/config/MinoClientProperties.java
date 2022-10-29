package com.ynthm.autoconfigure.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ethan Wang
 */
@ConfigurationProperties(prefix = "minio")
@Data
public class MinoClientProperties {

  /** api address */
  private String endpoint;
  /** 用户名 */
  private String accessKey;
  /** 密码 */
  private String secretKey;

  private String region;

  private int port = -1;
  /** 安全 */
  private boolean secure;

  private String defaultBucketName;

  private long partSize = 104857600;
}
