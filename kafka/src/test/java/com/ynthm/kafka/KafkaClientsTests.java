package com.ynthm.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.*;

@SpringBootTest
public class KafkaClientsTests {

    public static final String topic = "topic-test-01";

    private Map<String, Object> consumerProps(String group) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }


    @Test
    void testProducer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");   //required
        // 当值为0时producer不需要等待任何确认消息。当值为1时只需要等待leader确认。当值为-1或all时需要全部ISR集合返回确认才可以返回成功。
        props.put("acks", "all");   //required
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");  //required
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); //required
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(new ProducerRecord<>(topic, "value_" + i));
        }

        List<PartitionInfo> partitionInfos = kafkaProducer.partitionsFor(topic);
        partitionInfos.forEach(System.out::println);

        kafkaProducer.close(Duration.ofMinutes(100));

    }

    @Test
    void autoCommitConsumerDemo() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }

    /**
     * 手动提交 offset
     * 至少一次的消费语义 at least once
     * 当手动提交 offset 失败， 会重复消费
     */
    @Test
    void testManualCommitOffset() {
        String topic = "topic-test-01";
        String group = "group-01";

        Properties props = new Properties();
        // node01:9092,node03:9092
        props.put("bootstrap.servers", "localhost:9092");   //required
        props.put("group.id", group);   //required
        props.put("enable.auto.commit", "false"); // 关闭自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");     //从最早的消息开始读取
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //required
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //required

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));       //订阅topic
        final int minBatchSize = 10;
        // 缓存
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>(minBatchSize);
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(buffer::add);

                // 缓存满了才对数据进行处理
                if (buffer.size() >= minBatchSize) {

                    // 业务逻辑--插入数据库
                    // insertIntoDb(buffer);
                    // 等数据插入数据库之后，再异步提交位移

                    // 通过异步的方式提交位移
                    consumer.commitAsync(((offsets, exception) -> {
                        if (exception == null) {
                            offsets.forEach((topicPartition, metadata) -> {
                                System.out.println(topicPartition + " -> offset=" + metadata.offset());
                            });
                        } else {
                            exception.printStackTrace();
                            // 如果出错了，同步提交位移
                            consumer.commitSync(offsets);
                        }
                    }));


                    // 如果提交位移失败了，那么重启consumer后会重复消费之前的数据，再次插入到数据库中
                    // 清空缓冲区
                    buffer.clear();
                }
            }
        } finally {
            consumer.close();
        }
    }


    /**
     * 实现最多一次语义
     * 在消费前提交位移，当后续业务出现异常时，可能丢失数据
     * https://www.cnblogs.com/yn-huang/p/11684688.html
     */
    @Test
    public void testAtMostOnce() {

        String topic = "topic-test-01";
        String group = "group-01";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");   //required
        props.put("group.id", group);   //required
        props.put("enable.auto.commit", "false"); // 关闭自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "latest");     //从最早的消息开始读取
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //required
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //required
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        try {

            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500));
                // 处理业务之前就提交位移
                kafkaConsumer.commitAsync();
                // 下面是业务逻辑
                records.forEach(record -> {
                    System.out.println(record.value() + ", offset=" + record.offset());
                });
            }
        } catch (Exception e) {

        } finally {
            kafkaConsumer.close();
        }

    }
}
