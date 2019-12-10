package com.ynthm.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Author : Ynthm
 */
@Slf4j
@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "xiaoha", groupId = "group_id")
    public void consume(String message) {
        LOGGER.info("## consume message: {}", message);
    }

    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
    public void processMessage(ConsumerRecord<Integer, String> record) {
        log.info("kafka processMessage start");
        log.info("processMessage, topic = {}, msg = {}", record.topic(), record.value());
        // do something ...

        log.info("kafka processMessage end");
    }

}
