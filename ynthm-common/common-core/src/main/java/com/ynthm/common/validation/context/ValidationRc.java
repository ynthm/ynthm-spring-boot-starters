package com.ynthm.common.validation.context;

import com.ynthm.common.exception.UtilException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ValidationRc<T> {
  private final boolean valid;
  private final String error;
  private final T contextData; // 验证成功后的上下文数据
  private final Map<String, Object> intermediateValues; // 中间数据存储

  // 私有构造器
  private ValidationRc(
      boolean valid, String error, T contextData, Map<String, Object> intermediateValues) {
    this.valid = valid;
    this.error = error;
    this.contextData = contextData;
    this.intermediateValues = intermediateValues;
  }

  // 验证成功（带上下文）
  public static <T> ValidationRc<T> success(T contextData, Map<String, Object> intermediateValues) {
    return new ValidationRc<>(true, null, contextData, intermediateValues);
  }

  // 验证失败
  public static <T> ValidationRc<T> fail(String error) {
    return new ValidationRc<>(false, error, null, null);
  }

  // 获取上下文数据（验证成功时可用）
  public Optional<T> getContextData() {
    return Optional.ofNullable(contextData);
  }

  // 获取中间值
  public <V> Optional<V> getIntermediateValue(String key, Class<V> type) {
    if (intermediateValues == null) return Optional.empty();
    Object value = intermediateValues.get(key);
    return (type.isInstance(value)) ? Optional.of(type.cast(value)) : Optional.empty();
  }

  // 如果验证失败抛出异常
  public T getOrThrow() {
    if (!valid) {
      throw new UtilException(error);
    }
    return contextData;
  }

  // 将验证结果转换为业务处理流程
  public <R> R process(Function<T, R> processor) {
    if (!valid) {
      throw new UtilException(error);
    }
    return processor.apply(contextData);
  }
}
