package com.ynthm.starter.rocketmq.util;


import com.ynthm.starter.rocketmq.config.EnvironmentIsolationProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.util.StringUtils;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnvRocketMqClient extends RocketMqClient {
  private final EnvironmentIsolationProperties environmentIsolationProperties;

  public EnvRocketMqClient(RocketMQTemplate rocketMQTemplate, EnvironmentIsolationProperties environmentIsolationProperties) {
    super(rocketMQTemplate);
    this.environmentIsolationProperties = environmentIsolationProperties;
  }

  /**
   * 根据系统上下文自动构建隔离后的 topic 构建目的地
   */
  @Override
  public String buildDestination(String topic, String tag) {
    topic = reBuildTopic(topic);
    return MqUtil.buildDestination(topic, tag);
  }

  /**
   * 根据环境重新隔离topic
   *
   * @param topic 原始topic
   */
  private String reBuildTopic(String topic) {
    if (environmentIsolationProperties.isEnabled()
            && StringUtils.hasText(environmentIsolationProperties.getEnv())) {
      return topic + "_" + environmentIsolationProperties.getEnv();
    }
    return topic;
  }
}
