package com.ynthm.excel.demo.user.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
@Table(name = "t_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private Integer age;
  private LocalDate birth;
  private LocalDateTime createTime;
  private BigDecimal amount;
}
