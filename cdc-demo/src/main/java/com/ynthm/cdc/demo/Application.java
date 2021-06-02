package com.ynthm.cdc.demo;

import com.ynthm.avro.Person;
import com.ynthm.avro.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/** @author Ethan */
@Slf4j
@SpringBootApplication
public class Application implements ApplicationRunner {

  @Autowired private KafkaTemplate<String, User> kafkaTemplate;
  @Autowired private KafkaTemplate<String, Person> personKafkaTemplate;

  public static void main(String[] args) {
    System.setProperty("spring.devtools.restart.enabled", "false");
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    User user = User.newBuilder().setName("Charlie").setAge(10).build();
    kafkaTemplate.send("topic_test", String.valueOf(user.getName()), user);

    Person person =
        Person.newBuilder()
            .setName("Ethan")
            .setFavoriteColor("Yellow")
            .setFavoriteNumber(7)
            .build();

    ListenableFuture<SendResult<String, Person>> topicPerson =
        personKafkaTemplate.send("topic_person", person.getName().toString(), person);
    topicPerson.addCallback(
        result -> {
          assert result != null;
          log.info(
              "生产者成功发送消息到topic:{} partition:{}的消息",
              result.getRecordMetadata().topic(),
              result.getRecordMetadata().partition());
        },
        ex -> log.error("生产者发送消失败，原因：{}", ex.getMessage()));
  }
}
