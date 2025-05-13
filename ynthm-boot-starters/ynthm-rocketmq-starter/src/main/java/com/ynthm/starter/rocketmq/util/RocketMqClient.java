package com.ynthm.starter.rocketmq.util;

import com.ynthm.starter.rocketmq.domain.DelayLevel;
import com.ynthm.starter.rocketmq.domain.Msg;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ethan Wang
 * @version 1.0
 */

public class RocketMqClient {

  private final static Logger log = LoggerFactory.getLogger(RocketMqClient.class);

  private final RocketMQTemplate rocketMQTemplate;

  private final int sendMsgTimeout;

  public RocketMqClient(RocketMQTemplate rocketMQTemplate) {
    this.rocketMQTemplate = rocketMQTemplate;
    this.sendMsgTimeout = rocketMQTemplate.getProducer().getSendMsgTimeout();
  }

  public RocketMQTemplate getTemplate() {
    return this.rocketMQTemplate;
  }

  /**
   * 根据系统上下文自动构建隔离后的 topic 构建目的地
   */
  protected String buildDestination(String topic, String tag) {
    return MqUtil.buildDestination(topic, tag);
  }

  /**
   * 发送同步消息
   *
   * @param topic 消息所属 topic 的名称
   * @param tag   消息标签，方便服务器过滤使用。目前只支持每个消息设置一个
   */
  public <T> SendResult send(String topic, String tag, Msg<T> message) {
    // 注意分隔符
    return send(buildDestination(topic, tag), message);
  }

  /**
   * <p> 在同步模式下发送消息。此方法仅在发送过程完全完成时返回。
   * 可靠的同步传输被运用于广泛的场景，如重要的消息通知、短信营销体系等。</p>
   * <p> <strong> 警告:<strong> 此方法具有内部重试机制，即内部实现在声明失败之前会重试 {@link DefaultMQProducer#getRetryTimesWhenSendFailed()} 次。
   * 因此，多个消息可能潜在地传递给 (多个) 代理。由应用程序开发人员来解决潜在的重复问题。
   *
   * @param destination formats: `topicName:tags`
   * @param message     {@link  Msg<T>}
   * @return {@link SendResult}
   */
  public <T> SendResult send(String destination, Msg<T> message) {
    SendResult sendResult = rocketMQTemplate.syncSend(destination, message.build());
    // 此处为了方便查看给日志转了json，根据选择选择日志记录方式，例如ELK采集
    log.debug("[{}]同步消息[{}]发送结果[{}]", destination, message, sendResult);
    return sendResult;
  }


  /**
   * 批量发送同步消息
   *
   * @param topic    消息所属 topic 的名称
   * @param tag      消息标签，方便服务器过滤使用。目前只支持每个消息设置一个
   * @param messages Collection of message {@link  Msg<T>}
   */
  public <T> SendResult send(String topic, String tag, Collection<Msg<T>> messages) {
    return send(buildDestination(topic, tag), messages, sendMsgTimeout);
  }

  /**
   * 同步批量发送消息 in a given timeout.
   *
   * @param destination formats: `topicName:tags`
   * @param messages    Collection of message {@link  Msg<T>}
   * @param timeout     send timeout with millis
   * @return SendResult
   */
  public <T> SendResult send(String destination, Collection<Msg<T>> messages, long timeout) {
    return rocketMQTemplate.syncSend(destination, messages.stream()
            .map(Msg<T>::build).collect(Collectors.toList()), timeout);
  }

  /**
   * 发送延迟消息
   *
   * @param topic      topic name
   * @param tag        tags
   * @param message    {@link  Msg<T>}
   * @param delayLevel {@link DelayLevel} 延迟等级
   */
  public <T> SendResult send(
          String topic, String tag, Msg<T> message, int delayLevel) {
    return send(buildDestination(topic, tag), message, sendMsgTimeout, delayLevel);
  }

  /**
   * Same to {@link #send(String, Msg<T>)} with send timeout specified in addition.
   *
   * @param destination formats: `topicName:tags`
   * @param message     {@link  Msg<T>}
   * @param timeout     send timeout with millis
   * @param delayLevel  level for the delay message  {@link DelayLevel}
   * @return {@link SendResult}
   */
  public <T> SendResult send(String destination, Msg<T> message, long timeout, int delayLevel) {
    SendResult sendResult = rocketMQTemplate.syncSend(destination, message.build(), timeout, delayLevel);
    log.debug("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, message, sendResult);
    return sendResult;
  }

  /**
   * @param topic   消息所属 topic 的名称
   * @param tags    消息标签
   * @param message {@link  Msg<T>}
   * @param hashKey use this key to select queue. for example: orderId, productId ...
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String topic, String tags, Msg<T> message, String hashKey) {
    return sendOrderly(buildDestination(topic, tags), message, hashKey, sendMsgTimeout);
  }

  /**
   * @param destination formats: `topicName:tags`
   * @param message     {@link  Msg<T>}
   * @param hashKey     use this key to select queue. for example: orderId, productId ...
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String destination, Msg<T> message, String hashKey) {
    return sendOrderly(destination, message, hashKey, sendMsgTimeout);
  }

  /**
   * @param destination formats: `topicName:tags`
   * @param message     {@link  Msg<T>}
   * @param hashKey     use this key to select queue. for example: orderId, productId ...
   * @param timeout     send timeout with millis
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String destination, Msg<T> message, String hashKey, long timeout) {
    return sendOrderly(destination, message, hashKey, timeout, 0);
  }

  /**
   * Same to {@link #sendOrderly(String, Msg, String)} with send timeout specified in addition.
   *
   * @param destination formats: `topicName:tags`
   * @param message     {@link  Msg<T>}
   * @param hashKey     use this key to select queue. for example: orderId, productId ...
   * @param timeout     send timeout with millis
   * @param delayLevel  level for the delay message  {@link DelayLevel}
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String destination, Msg<T> message, String hashKey, long timeout,
                                    int delayLevel) {
    if (Objects.isNull(message)) {
      log.error("syncSendOrderly failed. destination:{}, message is null ", destination);
      throw new IllegalArgumentException("`message` cannot be null");
    }
    return rocketMQTemplate.syncSendOrderly(destination, message.build(), hashKey, timeout, delayLevel);
  }

  /**
   * 同步批量顺序发送消息
   *
   * @param topic    消息所属 topic 的名称
   * @param tag      消息标签
   * @param messages Collection of message {@link  Msg<T>}
   * @param hashKey  use this key to select queue. for example: orderId, productId ...
   * @return {@link SendResult}
   */

  public <T> SendResult sendOrderly(String topic, String tag, Collection<Msg<T>> messages,
                                    String hashKey) {
    return sendOrderly(buildDestination(topic, tag), messages, hashKey, sendMsgTimeout);
  }

  /**
   * 同步批量顺序发送消息
   *
   * @param destination formats: `topicName:tags`
   * @param messages    Collection of message {@link  Msg<T>}
   * @param hashKey     use this key to select queue. for example: orderId, productId ...
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String destination, Collection<Msg<T>> messages, String hashKey) {
    return sendOrderly(destination, messages, hashKey, sendMsgTimeout);
  }

  /**
   * 同步批量顺序发送消息
   *
   * @param destination formats: `topicName:tags`
   * @param messages    Collection of {@link  Msg<T>}
   * @param hashKey     use this key to select queue. for example: orderId, productId ...
   * @param timeout     send timeout with millis
   * @return {@link SendResult}
   */
  public <T> SendResult sendOrderly(String destination, Collection<Msg<T>> messages, String hashKey,
                                    long timeout) {
    if (Objects.isNull(messages)) {
      throw new IllegalArgumentException("`messages` can not be empty");
    }
    return rocketMQTemplate.syncSendOrderly(destination,
            messages.stream().map(Msg<T>::build).collect(Collectors.toList()), hashKey, timeout);
  }

  /**
   * 在事务中发送消息
   *
   * @param topic   消息所属 topic 的名称
   * @param tag     消息标签
   * @param message message {@link  Msg<T>}
   * @param arg     ext arg
   * @return TransactionSendResult
   */
  public <T> TransactionSendResult sendMessageInTransaction(String topic, String tag, Msg<T> message,
                                                            final Object arg) {
    return sendMessageInTransaction(buildDestination(topic, tag), message, arg);
  }

  /**
   * 在事务中发送消息
   *
   * @param destination destination formats: `topicName:tags`
   * @param message     message {@link  Msg<T>}
   * @param arg         ext arg
   * @return TransactionSendResult
   */
  public <T> TransactionSendResult sendMessageInTransaction(String destination, Msg<T> message,
                                                            final Object arg) {
    return rocketMQTemplate.sendMessageInTransaction(destination, message.build(), arg);
  }
}
