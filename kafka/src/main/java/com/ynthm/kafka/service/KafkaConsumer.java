package com.ynthm.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Author : Ynthm
 */
@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "xiaoha", groupId = "group_id")
    public void consume(String message) {
        LOGGER.info("## consume message: {}", message);
    }
}
