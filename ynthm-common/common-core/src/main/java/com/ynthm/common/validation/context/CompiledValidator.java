package com.ynthm.common.validation.context;

import com.ynthm.common.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class CompiledValidator<T> {
  private final List<ValidationStep<T>> steps;

  public CompiledValidator(List<ValidationStep<T>> steps) {
    this.steps = steps;
  }

  public ValidationResult validate(T target) {
    ValidationContext<T> context = ValidationContext.of(target);
    for (ValidationStep<T> step : steps) {
      step.execute(context);
      if (context.hasFailed()) break;
    }
    return context.finish();
  }

  /**
   * 每个 ValidationStep 快速调用 ValidationContext.fail 然后 return 快速失败返回
   *
   * @param context 校验目标及上下文
   */
  public ValidationResult validate(ValidationContext<T> context) {
    for (ValidationStep<T> step : steps) {
      step.execute(context);
      if (context.hasFailed()) break;
    }
    return context.finish();
  }

  /**
   * 使用Builder模式创建
   *
   * @param <T>
   */
  public static class Builder<T> {
    private final List<ValidationStep<T>> steps = new ArrayList<>();

    public Builder<T> addStep(ValidationStep<T> step) {
      steps.add(step);
      return this;
    }

    public CompiledValidator<T> build() {
      return new CompiledValidator<>(steps);
    }
  }
}
