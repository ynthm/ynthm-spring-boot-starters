package com.ynthm.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.*;
import static org.springframework.kafka.test.assertj.KafkaConditions.partition;

@SpringBootTest
@EmbeddedKafka
public class KafkaApplicationTests {

    public static final String TOPIC1 = "topic001";
    public static final String TOPIC2 = "topic002";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    void testKeyValue() {
        ConsumerRecord<Integer, String> record = new ConsumerRecord<>("topic", 42, 0, 23, "foo");
        assertThat(record).has(allOf(value("foo"), partition(42)));
        record = new ConsumerRecord<>("topic", 42, 0, 23, null);
        assertThat(record).has(key(23));
//        record = new ConsumerRecord<>("topic", 42, 0, null, "foo");
//        assertThat(record).has(keyValue(null, "foo"));
//        record = new ConsumerRecord<>("topic", 42, 0, null, null);
//        assertThat(record).has(keyValue(null, null));
//        assertThat(record).doesNotHave(keyValue(23, null));
//        assertThat(record).doesNotHave(keyValue(null, "foo"));
//        record = null;
//        assertThat(record).doesNotHave(keyValue(null, null));

    }

}
