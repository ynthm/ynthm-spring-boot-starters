package com.ynthm.autoconfigure.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@ConfigurationProperties("jpush")
public class PushProperties {

  private String appKey;
  private String masterSecret;
  private int maxRetry = 3;

  /**
   * 一天
   */
  private int ttl = 60 * 60 * 24;

  /**
   * 连接超时时间 默认 10s
   */
  private int connectionTimeout = 10000;
  /**
   *  iOS 平台 APNs 推送通道 true-推送生产环境 false-推送开发环境（测试使用参数）
   */
  private boolean apnsProduction = false;
}
