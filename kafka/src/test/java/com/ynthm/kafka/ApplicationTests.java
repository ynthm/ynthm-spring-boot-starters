package com.ynthm.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// @EmbeddedKafka
public class ApplicationTests {

  public static final String TOPIC1 = "topic001";
  public static final String TOPIC2 = "topic002";

  // @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Test
  void testKeyValue() {
    //    ConsumerRecord<Integer, String> record = new ConsumerRecord<>("topic", 42, 0, 23, "foo");
    //    assertThat(record).has(allOf(value("foo"), partition(42)));
    //    record = new ConsumerRecord<>("topic", 42, 0, 23, null);
    //    assertThat(record).has(key(23));
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
