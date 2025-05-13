package com.ynthm.starter.rocketmq.domain;

import java.time.LocalDateTime;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface IMsg {
  /** 业务唯一键 */
  String key();

  /** 发送消息来源，用于排查问题 */
  String source();

  /** 发送时间 */
  LocalDateTime sendTime();

  default Message<IMsg> build() {
    return MessageBuilder.withPayload(this).setHeader(RocketMQHeaders.KEYS, key()).build();
  }
}
