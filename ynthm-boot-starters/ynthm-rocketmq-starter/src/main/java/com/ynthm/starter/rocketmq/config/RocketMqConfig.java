package com.ynthm.starter.rocketmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ynthm.starter.rocketmq.util.EnvRocketMqClient;
import com.ynthm.starter.rocketmq.util.RocketMqClient;
import java.util.List;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.StringUtils;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties({EnvironmentIsolationProperties.class})
public class RocketMqConfig {

  @Bean
  public RocketMqClient rocketMqClient(RocketMQTemplate rocketMQTemplate,
                                       EnvironmentIsolationProperties environmentIsolationProperties) {
    if (environmentIsolationProperties.isEnabled()) {
      return new EnvRocketMqClient(rocketMQTemplate, environmentIsolationProperties);
    }
    return new RocketMqClient(rocketMQTemplate);
  }

  @ConditionalOnProperty(name = "rocketmq.isolation.enabled", havingValue = "true")
  @Bean
  public BeanPostProcessor envIsolation(EnvironmentIsolationProperties config) {
    return new BeanPostProcessor() {
      @SuppressWarnings("NullableProblems")
      @Override
      public Object postProcessBeforeInitialization(Object bean, String beanName)
              throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
          DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
          String env = config.getEnv();
          // 拼接Topic
          if (config.isEnabled() && StringUtils.hasText(env)) {
            container.setTopic(String.join(MqConst.UNDERSCORE, container.getTopic(), env));
          }
          return container;
        }

        return bean;
      }
    };
  }

  /**
   * 解决 RocketMQ Jackson 不支持 Java 8 时间类型配置
   *
   * <p>源码参考：{@link org.apache.rocketmq.spring.autoconfigure.MessageConverterConfiguration}
   */
  @Bean
  @Primary
  public RocketMQMessageConverter createRocketMQMessageConverter() {
    RocketMQMessageConverter converter = new RocketMQMessageConverter();
    CompositeMessageConverter compositeMessageConverter =
            (CompositeMessageConverter) converter.getMessageConverter();
    List<MessageConverter> messageConverterList = compositeMessageConverter.getConverters();
    for (MessageConverter messageConverter : messageConverterList) {
      if (messageConverter instanceof MappingJackson2MessageConverter) {
        MappingJackson2MessageConverter jackson2MessageConverter =
                (MappingJackson2MessageConverter) messageConverter;
        ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
        // 增加Java8时间模块支持，实体类可以传递LocalDate/LocalDateTime
        objectMapper.registerModules(new JavaTimeModule());
      }
    }
    return converter;
  }
}
