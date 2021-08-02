package com.ynthm.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.kafka.domain.Greeting;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : Ynthm 配置生产者 producerConfigs producerFactory KafkaTemplate 配置消费者 consumerConfigs
 * consumerFactory kafkaListenerContainerFactory
 */
@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaCustomProperties.class)
public class KafkaConfig {

  @Autowired private KafkaProperties kafkaProperties;
  @Autowired private KafkaCustomProperties kafkaCustomProperties;

  @Value("${kafka.producer.bootstrapServers}")
  private String producerBootstrapServers; // 生产者连接Server地址

  @Value("${kafka.producer.retries}")
  private String producerRetries; // 生产者重试次数

  @Value("${kafka.producer.batchSize}")
  private String producerBatchSize;

  @Value("${kafka.producer.lingerMs}")
  private String producerLingerMs;

  @Value("${kafka.producer.bufferMemory}")
  private String producerBufferMemory;

  @Value("${kafka.consumer.bootstrapServers}")
  private String consumerBootstrapServers;

  @Value("${kafka.consumer.groupId}")
  private String consumerGroupId;

  @Value("${kafka.consumer.enableAutoCommit}")
  private String consumerEnableAutoCommit;

  @Value("${kafka.consumer.autoCommitIntervalMs}")
  private String consumerAutoCommitIntervalMs;

  @Value("${kafka.consumer.sessionTimeoutMs}")
  private String consumerSessionTimeoutMs;

  @Value("${kafka.consumer.maxPollRecords}")
  private String consumerMaxPollRecords;

  @Value("${kafka.consumer.autoOffsetReset}")
  private String consumerAutoOffsetReset;

  @Bean
  public NewTopic topic1() {
    return new NewTopic(TopicName.TOPIC_DEMO_001, 10, (short) 1);
  }

  @Bean
  public ObjectMapper ObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

  private Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
    props.put(ProducerConfig.RETRIES_CONFIG, 1);
    props.put(ProducerConfig.ACKS_CONFIG, kafkaCustomProperties.getProducer().getAcks());
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return props;
  }

  private Map<String, Object> consumerProperties() {
    Map<String, Object> configs = new HashMap<>();
    KafkaProperties.Consumer consumer = kafkaCustomProperties.getConsumer();
    configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.getBootstrapServers());
    configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
    configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer.getEnableAutoCommit());
    configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerAutoCommitIntervalMs);
    configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
    configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords);
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
    configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return configs;
  }

  public ProducerFactory<String, String> customProducerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(
      ProducerFactory<String, String> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  @Qualifier("customKafkaTemplate")
  public KafkaTemplate<String, String> customKafkaTemplate() {
    return new KafkaTemplate<>(customProducerFactory());
  }

  /**
   * MANUAL 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
   *
   * @param consumerFactory
   * @return
   */
  @Bean("manualListenerContainerFactory")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
      manualListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.getContainerProperties().setPollTimeout(1500);
    factory.setBatchListener(true);
    factory.setConcurrency(3);
    // 配置手动提交offset
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    return factory;
  }

  /**
   * MANUAL_IMMEDIATE 手动调用Acknowledgment.acknowledge()后立即提交
   *
   * @param consumerFactory
   * @return
   */
  @Bean("manualImmediateListenerContainerFactory")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
      manualImmediateListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.getContainerProperties().setPollTimeout(1500);
    factory.setBatchListener(true);
    // 配置手动提交offset
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    return factory;
  }

  /**
   * COUNT 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
   *
   * @param consumerFactory
   * @return
   */
  @Bean("countListenerContainerFactory")
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
      countListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.getContainerProperties().setPollTimeout(1500);
    factory.getContainerProperties().setAckCount(5);
    // 配置手动提交offset
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.COUNT);
    return factory;
  }

  public ProducerFactory<String, Greeting> greetingProducerFactory() {
    Map<String, Object> producerConfigs = producerConfigs();
    producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(producerConfigs);
  }

  @Bean
  @Qualifier("greetingKafkaTemplate")
  public KafkaTemplate<String, Greeting> greetingKafkaTemplate() {
    return new KafkaTemplate<>(greetingProducerFactory());
  }

  public ConsumerFactory<String, Greeting> greetingConsumerFactory() {
    Map<String, Object> props = consumerProperties();
    return new DefaultKafkaConsumerFactory<>(
        props, new StringDeserializer(), new JsonDeserializer<>(Greeting.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Greeting>
      greetingKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Greeting> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(greetingConsumerFactory());
    return factory;
  }
}
