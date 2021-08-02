package com.ynthm.kafka.domain;

import java.time.LocalDateTime;

/** @author : Ynthm Wang */
public class Order {
  private long id;
  private String orderNum;
  private LocalDateTime createdTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(String orderNum) {
    this.orderNum = orderNum;
  }

  public LocalDateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(LocalDateTime createdTime) {
    this.createdTime = createdTime;
  }
}
