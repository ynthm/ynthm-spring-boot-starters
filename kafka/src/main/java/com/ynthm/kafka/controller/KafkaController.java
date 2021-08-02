package com.ynthm.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ynthm.kafka.config.TopicName;
import com.ynthm.kafka.domain.Greeting;
import com.ynthm.kafka.domain.Order;
import com.ynthm.kafka.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaController {

  @Autowired private ProducerService producerService;

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

  @PostMapping("/greeting")
  public void producer(@RequestBody Greeting greeting) throws JsonProcessingException {

    greetingKafkaTemplate.send(TopicName.TOPIC_GREETING, new Greeting("Hello", "World"));
  }

  @RequestMapping("/producer")
  public void producer(@RequestBody Order order) throws JsonProcessingException {

    producerService.sendMessage(order);
  }

  @GetMapping("/producer/test1")
  public void test(String msg) {
    sendMessage(msg);
  }

  public void sendMessage(String message) {

    ListenableFuture<SendResult<String, String>> future =
        kafkaTemplate.send(TopicName.TOPIC_DEMO_001, message);

    future.addCallback(
        new ListenableFutureCallback<SendResult<String, String>>() {

          @Override
          public void onSuccess(SendResult<String, String> result) {
            System.out.println(
                "Sent message=["
                    + message
                    + "] with offset=["
                    + result.getRecordMetadata().offset()
                    + "]");
          }

          @Override
          public void onFailure(Throwable ex) {
            System.out.println(
                "Unable to send message=[" + message + "] due to : " + ex.getMessage());
          }
        });
  }
}
