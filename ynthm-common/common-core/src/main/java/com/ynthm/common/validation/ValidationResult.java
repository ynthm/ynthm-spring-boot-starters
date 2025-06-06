package com.ynthm.common.validation;

import lombok.Getter;

/**
 * 快速失败 单错误消息
 *
 * @author Ethan Wang
 * @version 1.0
 */
@Getter
public class ValidationResult {
  /** 验证有效 */
  private final boolean valid;

  private final String message;

  public ValidationResult(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  public static ValidationResult valid() {
    return new ValidationResult(true, null);
  }

  public static ValidationResult invalid(String error) {
    return new ValidationResult(false, error);
  }

  /** 无效 */
  public boolean invalid() {
    return !valid;
  }
}
