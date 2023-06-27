package com.ynthm.common.web.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author Ethan Wang
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("okhttp")
public class OkHttpProperties implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long connectTimeout;
  private Long readTimeout;
  private Long writeTimeout;

  private Integer maxIdleConnections;
  private Long keepAliveDuration;
}
