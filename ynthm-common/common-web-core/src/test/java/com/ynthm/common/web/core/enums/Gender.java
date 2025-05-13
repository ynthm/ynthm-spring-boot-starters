package com.ynthm.common.web.core.enums;


/**
 * @author Ethan Wang
 * @version 1.0
 */
public enum Gender implements Enumerator<Integer> {
  FEMALE(0, "女"),
  MALE(1, "男"),
  OTHER(2, "其他")
  ;

  private final int value;
  private final String label;

  Gender(int value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  public Integer value() {
    return value;
  }

  @Override
  public String label() {
    return label;
  }
}
