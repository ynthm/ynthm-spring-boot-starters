package com.ynthm.elasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/3 10:19 上午
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
public class RestClientProperties {
  private List<String> uris;

  /** = Duration.ofSeconds(60) */
  private Duration readTimeout;
}
