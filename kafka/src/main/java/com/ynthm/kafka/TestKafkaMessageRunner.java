package com.ynthm.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

@Slf4j
public class TestKafkaMessageRunner implements CommandLineRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    /**
     *
     * id 线程名前缀
     * @param cr
     * @throws Exception
     */
    @KafkaListener(id = "test-consumer", topics = {"topic.test001"})
    public void listen(ConsumerRecord<String, String> cr) throws Exception {
        log.debug(Thread.currentThread().getName() + "  {}", cr.value());
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("start producer 10000 message");
        for (int i = 0; i < 1000; i++) {
            kafkaTemplate.send("topic.test001", UUID.randomUUID().toString(), "i" + i);
        }


    }
}
