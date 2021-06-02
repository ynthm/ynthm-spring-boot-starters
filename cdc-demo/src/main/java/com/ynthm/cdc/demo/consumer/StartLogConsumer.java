package com.ynthm.cdc.demo.consumer;

import com.ynthm.avro.cf88_trex.cf_app_startlog.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/** @author Ethan */
@Slf4j
@Service
public class StartLogConsumer {
  @KafkaListener(topics = "com.ynthm.avro.cf88_trex.cf_app_startlog")
  public void test(ConsumerRecord<String, Envelope> cr, Acknowledgment acknowledgment) {
    log.info("start log {}", cr.value());
    acknowledgment.acknowledge();
  }
}
