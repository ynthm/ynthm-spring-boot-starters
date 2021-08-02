package com.ynthm.kafka.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 反序列化用到无参构造函数
 *
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Greeting {
  private String name;
  private String msg;
}
