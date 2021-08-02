package com.ynthm.kafka.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaCustomProperties {
  private List<String> bootstrapServers =
      new ArrayList(Collections.singletonList("localhost:9092"));
  private String clientId;
  private final Map<String, String> properties = new HashMap();
  private final KafkaProperties.Consumer consumer = new KafkaProperties.Consumer();
  private final KafkaProperties.Producer producer = new KafkaProperties.Producer();
}
