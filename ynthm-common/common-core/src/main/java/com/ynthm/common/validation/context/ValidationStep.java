package com.ynthm.common.validation.context;

/**
 * 定义校验步骤接口
 *
 * @author Ethan Wang
 * @version 1.0
 */
@FunctionalInterface
public interface ValidationStep<T> {
  void execute(ValidationContext<T> context);
}
