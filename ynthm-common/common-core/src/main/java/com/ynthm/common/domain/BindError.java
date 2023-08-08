package com.ynthm.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BindError extends ErrorItem {
  private String field;

  public BindError(String code, String message, String field) {
    this.code = code;
    this.msg = message;
    this.field = field;
  }
}
