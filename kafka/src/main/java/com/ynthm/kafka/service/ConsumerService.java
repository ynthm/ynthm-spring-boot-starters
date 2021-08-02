package com.ynthm.kafka.service;

import com.ynthm.kafka.config.TopicName;
import com.ynthm.kafka.domain.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/** @author Ynthm Wang */
@Slf4j
@Component
public class ConsumerService {

  /** id 线程名前缀 id 设置后就是 group id */
  @KafkaListener(
      id = "test-consumer",
      topics = {TopicName.TOPIC_001})
  public void listen(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
      Acknowledgment ack) {
    log.debug(Thread.currentThread().getName() + "  {}", message);
    ack.acknowledge();
  }

  @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}")
  public void processMessage(
      @Header(KafkaHeaders.GROUP_ID) String groupId,
      ConsumerRecord<Integer, String> record,
      Acknowledgment ack) {
    log.info(
        "processMessage,group id:{} topic: {}, partition:{}, offset:{}",
        groupId,
        record.topic(),
        record.partition(),
        record.offset());
    // do something ...
    ack.acknowledge();
  }

  @KafkaListener(topics = {TopicName.TOPIC_001})
  public void topic001(
      @Header(KafkaHeaders.GROUP_ID) String groupId,
      ConsumerRecord<Integer, String> record,
      Acknowledgment ack) {
    log.info(
        "processMessage,group id:{} topic: {}, partition:{}, offset:{}",
        groupId,
        record.topic(),
        record.partition(),
        record.offset());
    ack.acknowledge();
  }

  @KafkaListener(topics = {TopicName.TOPIC_002})
  public void topic002(
      @Header(KafkaHeaders.GROUP_ID) String groupId,
      ConsumerRecord<Integer, String> record,
      Acknowledgment ack) {
    log.info(
        "processMessage,group id:{} topic: {}, partition:{}, offset:{}",
        groupId,
        record.topic(),
        record.partition(),
        record.offset());
    ack.acknowledge();
  }

  @KafkaListener(topics = {TopicName.TOPIC_003})
  public void topic003(
      @Header(KafkaHeaders.GROUP_ID) String groupId,
      ConsumerRecord<Integer, String> record,
      Acknowledgment ack) {
    log.info(
        "processMessage,group id:{} topic: {}, partition:{}, offset:{}",
        groupId,
        record.topic(),
        record.partition(),
        record.offset());
    ack.acknowledge();
  }

  /**
   * MANUAL 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
   *
   * @param message
   * @param ack
   */
  @KafkaListener(
      containerFactory = "manualListenerContainerFactory",
      topics = TopicName.TOPIC_MANUAL)
  public void onMessageManual(List<Object> message, Acknowledgment ack) {
    log.info("manualListenerContainerFactory 处理数据量：{}", message.size());
    message.forEach(item -> log.info("manualListenerContainerFactory 处理数据内容：{}", item));
    // 直接提交offset
    ack.acknowledge();
  }

  /**
   * MANUAL_IMMEDIATE 手动调用Acknowledgment.acknowledge()后立即提交
   *
   * @param message
   */
  @KafkaListener(
      containerFactory = "manualImmediateListenerContainerFactory",
      topics = TopicName.TOPIC_MANUAL_IMMEDIATE)
  public void onMessageManualImmediate(List<Object> message, Acknowledgment ack) {
    log.info("manualImmediateListenerContainerFactory 处理数据量：{}", message.size());
    message.forEach(item -> log.info("manualImmediateListenerContainerFactory 处理数据内容：{}", item));
    // 直接提交offset
    ack.acknowledge();
  }

  /**
   * COUNT 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
   *
   * @param message
   */
  @KafkaListener(containerFactory = "countListenerContainerFactory", topics = TopicName.TOPIC_COUNT)
  public void onMessageCount(List<Object> message) {
    log.info("countListenerContainerFactory 处理数据量：{}", message.size());
    message.forEach(item -> log.info("countListenerContainerFactory 处理数据内容：{}", item));
  }

  @KafkaListener(
      topics = TopicName.TOPIC_GREETING,
      containerFactory = "greetingKafkaListenerContainerFactory")
  public void greetingListener(Greeting greeting) {
    log.info(greeting.toString());
  }
}
