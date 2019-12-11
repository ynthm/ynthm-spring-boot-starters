package com.ynthm.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
//@Component
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties implements Serializable {

    private String groupId;
    private String[] topicName;
}
