package com.ynthm.kafka;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.ynthm.kafka.config.TopicName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** @author Ynthm Wang */
@Slf4j
@Component
public class TestKafkaMessageRunner implements CommandLineRunner {

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void run(String... args) throws Exception {

    //    for (int i = 0; i < 1000; i++) {
    //      kafkaTemplate.send(TopicName.TOPIC_001, UUID.randomUUID().toString(), "i" + i);
    //    }
    //    for (int i = 0; i < 1000; i++) {
    //      kafkaTemplate.send(TopicName.TOPIC_002, UUID.randomUUID().toString(), "i" + i);
    //    }
    //    for (int i = 0; i < 1000; i++) {
    //      kafkaTemplate.send(TopicName.TOPIC_003, UUID.randomUUID().toString(), "i" + i);
    //    }

    for (int i = 0; i < 100000; i++) {
      kafkaTemplate.send(TopicName.TOPIC_MANUAL, UUID.randomUUID().toString(), "i" + i);
    }

    ScheduledExecutorService executorService =
        new ScheduledThreadPoolExecutor(
            1,
            ThreadFactoryBuilder.create()
                .setNamePrefix("example-schedule-pool-")
                .setDaemon(true)
                .build());

    executorService.scheduleAtFixedRate(
        () -> {
          kafkaTemplate
              .send(TopicName.TOPIC_MANUAL, String.valueOf(System.currentTimeMillis()))
              .addCallback(
                  result -> {
                    if (null != result.getRecordMetadata()) {
                      System.out.println("消费发送成功 offset:" + result.getRecordMetadata().offset());
                      return;
                    }
                    System.out.println("消息发送成功");
                  },
                  throwable -> System.out.println("消费发送失败:" + throwable.getMessage()));
        },
        0,
        1,
        TimeUnit.SECONDS);

    log.info("producer message");
  }
}
