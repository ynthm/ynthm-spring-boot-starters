package com.ynthm.common.validation.simple;

import com.ynthm.common.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class FailFastValidator<T> {
  private final List<ValidationRule<T>> rules = new ArrayList<>();

  public void addRule(ValidationRule<T> rule) {
    rules.add(rule);
  }

  /**
   * 短路模式（遇到第一个错误即返回）
   */
  public ValidationResult validate(T data) {
    for (ValidationRule<T> rule : rules) {
      ValidationResult result = rule.validate(data);
      if (!result.isValid()) {
        return result;
      }
    }
    return ValidationResult.valid();
  }
}
