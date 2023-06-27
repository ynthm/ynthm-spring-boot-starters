package com.ynthm.common.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
public class OrderEntity {
  private int id;
  private String name;
  private LocalDateTime createTime;
  private BigDecimal amount;
}
