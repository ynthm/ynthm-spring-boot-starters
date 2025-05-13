package com.ynthm.starter.rocketmq.domain;

import java.time.LocalDateTime;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class Msg<T> {
  /** 业务唯一键 */
  protected String key;

  /** 发送消息来源，用于排查问题 */
  protected String src = "";

  /** 发送时间 */
  protected LocalDateTime st = LocalDateTime.now();

  /** 业务数据 body */
  protected T body;

  public String getKey() {
    return key;
  }

  public Msg<T> setKey(String key) {
    this.key = key;
    return this;
  }

  public String getSrc() {
    return src;
  }

  public Msg<T> setSrc(String src) {
    this.src = src;
    return this;
  }

  public LocalDateTime getSt() {
    return st;
  }

  public Msg<T> setSt(LocalDateTime st) {
    this.st = st;
    return this;
  }

  public T getBody() {
    return body;
  }

  public Msg<T> setBody(T body) {
    this.body = body;
    return this;
  }

  public Message<Msg<T>> build() {
    // 设置 KEYS
    return MessageBuilder.withPayload(this).setHeader(RocketMQHeaders.KEYS, key).build();
  }
}
