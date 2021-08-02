package com.ynthm.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.kafka.config.TopicName;
import com.ynthm.kafka.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/** @author Ynthm Wang */
@Component
public class ProducerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);

  @Autowired private ObjectMapper objectMapper;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  @Qualifier("customKafkaTemplate")
  private KafkaTemplate<String, String> customKafkaTemplate;

  public void sendMessage(Order order) throws JsonProcessingException {

    // 发送消息，订单类的 json 作为消息体
    ListenableFuture<SendResult<String, String>> future =
        kafkaTemplate.send(TopicName.TOPIC_002, objectMapper.writeValueAsString(order));
    // 监听回调
    future.addCallback(
        result -> {
          LOGGER.info("## Send message success ...");
        },
        ex -> {
          LOGGER.info("## Send message fail ...");
        });
  }
}
