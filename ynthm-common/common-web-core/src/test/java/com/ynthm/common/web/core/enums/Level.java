package com.ynthm.common.web.core.enums;


/**
 * @author Ethan Wang
 * @version 1.0
 */
public enum Level implements Enumerator<String> {
  BRONZE("1", "青銅"),
  KING("2", "王者"),
  ;

  private final String value;
  private final String label;

  Level(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public String label() {
    return label;
  }
}
