package com.ynthm.starter.rocketmq.domain;



/**
 * @author Ethan Wang
 * @version 1.0
 */

public enum DelayLevel {
  L1(1, "1s"),
  L2(2, "5s"),
  L3(3, "10s"),
  L4(4, "30s"),
  L5(5, "1m"),
  L6(6, "2m"),
  L7(7, "3m"),
  L8(8, "4m"),
  L9(9, "5m"),

  L10(10, "6m"),
  L11(11, "7m"),
  L12(12, "8m"),
  L13(13, "9m"),
  L14(14, "10m"),

  L15(15, "20m"),
  L16(16, "30m"),
  L17(17, "1h"),
  L18(18, "2h");

  /** 延迟等级 */
  private final int level;
  /** 延迟时间 */
  private final String label;

  DelayLevel(int level, String label) {
    this.level = level;
    this.label = label;
  }

  public int getLevel() {
    return level;
  }

  public String getLabel() {
    return label;
  }
}

