package com.ynthm.cdc.demo.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.avro.Person;
import com.ynthm.avro.User;
import com.ynthm.avro.office.t_customer.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CutomerConsumer {

  @Autowired private ObjectMapper om;

  /**
   * id 可以在日志中显示不同线程名
   *
   * @param cr 消费记录
   * @param acknowledgment 确认 offset
   */
  @KafkaListener(id = "c_test", topics = "#{'${cdc.topic.bo.customer}'}")
  @KafkaListener(topics = "com.ynthm.avro.office.t_customer")
  public void test(ConsumerRecord<String, Envelope> cr, Acknowledgment acknowledgment) {
    log.info("UTM term {}", cr.value().getAfter().getUtmTerm());
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "topic_test")
  public void consume(ConsumerRecord<String, User> cr, Acknowledgment acknowledgment) {
    log.info(String.format("Consumed message -> %s", cr.toString()));
    acknowledgment.acknowledge();
  }

  @KafkaListener(topics = "topic_person")
  public void person(ConsumerRecord<String, Person> cr, Acknowledgment acknowledgment) {
    log.info(String.format("Consumed message -> %s", cr.toString()));
    acknowledgment.acknowledge();
  }
}
