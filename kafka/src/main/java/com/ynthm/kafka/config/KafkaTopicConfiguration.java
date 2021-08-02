package com.ynthm.kafka.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author Ynthm Wang */
@Configuration
@EnableConfigurationProperties(KafkaTopicProperties.class)
public class KafkaTopicConfiguration {

  private final KafkaTopicProperties properties;

  public KafkaTopicConfiguration(KafkaTopicProperties properties) {
    this.properties = properties;
  }

  @Bean
  public String[] kafkaTopicName() {
    return properties.getTopicName();
  }

  @Bean
  public String topicGroupId() {
    return properties.getGroupId();
  }
}
