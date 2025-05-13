package com.ynthm.starter.rocketmq.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@ConditionalOnProperty(
    name = "rocketmq.isolation.enabled",
    havingValue = "true",
    matchIfMissing = true)
@ConfigurationProperties(prefix = "rocketmq.isolation")
public class EnvironmentIsolationProperties {
  private boolean enabled = false;
  private String env;

  public boolean isEnabled() {
    return enabled;
  }

  public EnvironmentIsolationProperties setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public String getEnv() {
    return env;
  }

  public EnvironmentIsolationProperties setEnv(String env) {
    this.env = env;
    return this;
  }
}
