package com.ynthm.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaClientsTests {


    @Test
    void testProducer() {

        String topic = "topic-test-01";
        String group = "group-01";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");   //required
        props.put("acks", "all");   //required
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //required
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //required
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(new ProducerRecord<>(topic, "value_" + i));
        }

        List<PartitionInfo> partitionInfos = kafkaProducer.partitionsFor(topic);
        partitionInfos.forEach(System.out::println);

        kafkaProducer.close(Duration.ofMinutes(100));

    }

    /**
     * 手动提交 offset
     * 至少一次的消费语义 at least once
     * 当手动提交 offset 失败， 会重复消费
     */
    @Test
    void testCommitOffset() {
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
