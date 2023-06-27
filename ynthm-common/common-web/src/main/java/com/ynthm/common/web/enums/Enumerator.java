package com.ynthm.common.web.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Ethan Wang
 */
public interface Enumerator {
  @JsonValue
  int value();
}
