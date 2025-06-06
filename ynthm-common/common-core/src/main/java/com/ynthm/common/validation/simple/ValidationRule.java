package com.ynthm.common.validation.simple;

import com.ynthm.common.validation.ValidationResult;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@FunctionalInterface
public interface ValidationRule<T> {
  ValidationResult validate(T value);
}
