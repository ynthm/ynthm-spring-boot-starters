package com.ynthm.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/** @author Ynthm Wang */
@Data
@ConfigurationProperties(prefix = "kafka-topic")
public class KafkaTopicProperties implements Serializable {

  private String groupId;
  private String[] topicName;
}
