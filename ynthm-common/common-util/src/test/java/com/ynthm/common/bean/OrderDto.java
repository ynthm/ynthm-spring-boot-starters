package com.ynthm.common.bean;

import lombok.Data;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
public class OrderDto {
  private int id;
  private String name;
  private String createTime;
  private String amount;
}
