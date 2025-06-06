package com.ynthm.common.validation.context;

import com.google.common.reflect.TypeToken;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.exception.UtilException;
import com.ynthm.common.util.CastUtil;
import com.ynthm.common.validation.ValidationResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class ValidationContext<T> {
  private final T target;
  private final Map<String, Object> intermediateData;
  private String errorMessage;
  private boolean failed;

  // 私有构造器，强制使用工厂方法
  private ValidationContext(T target) {
    this.target = target;
    this.intermediateData = new HashMap<>();
    this.failed = false;
  }

  /**
   * 创建校验上下文
   *
   * @param target 被校验的目标对象
   * @return 校验上下文实例
   */
  public static <T> ValidationContext<T> of(T target) {
    return new ValidationContext<>(target);
  }

  /**
   * 存储中间数据（类型安全）
   *
   * @param key 数据键名
   * @param value 数据值
   * @return 当前上下文（链式调用）
   */
  public <V> ValidationContext<T> store(String key, V value) {
    if (!failed) intermediateData.put(key, value);
    return this;
  }

  /**
   * 获取中间数据（类型安全）
   *
   * @param key 数据键名
   * @param type 数据类型Class
   * @return Optional包装的数据值
   */
  public <V> Optional<V> get(String key, Class<V> type) {
    Object value = intermediateData.get(key);
    if (type.isInstance(value)) {
      return Optional.of(type.cast(value));
    }
    return Optional.empty();
  }

  public <V> V getOrElseThrow(String key, Class<V> type) {
    Object value = intermediateData.get(key);
    if (type.isInstance(value)) {
      return type.cast(value);
    }
    throw new UtilException(BaseResultCode.NOT_EXIST_CONTEXT);
  }

  /** 使用TypeToken处理泛型集合 */
  public <V> Optional<V> get(String key, TypeToken<V> type) {
    Object value = intermediateData.get(key);
    if (value != null && type.getRawType().isInstance(value)) {
      return Optional.of(CastUtil.cast(type.getRawType().cast(value)));
    }
    return Optional.empty();
  }

  /**
   * 执行校验规则（快速失败）
   *
   * @param validator 校验函数 返回true跳过校验(快速失败)
   * @param errorMsg 失败时的错误信息
   * @return 当前上下文（链式调用）
   */
  public ValidationContext<T> validate(Predicate<ValidationContext<T>> validator, String errorMsg) {
    if (!failed && !validator.test(this)) {
      fail(errorMsg);
    }
    return this;
  }

  /**
   * 执行带转换的校验规则
   *
   * @param key 存储键名
   * @param converter 转换函数
   * @param validator 校验函数
   * @param errorMsg 失败时的错误信息
   * @return 当前上下文（链式调用）
   */
  public <V> ValidationContext<T> validateAndStore(
      String key, Function<T, V> converter, Predicate<V> validator, String errorMsg) {
    if (failed) return this;

    V value = converter.apply(target);
    if (!validator.test(value)) {
      return fail(errorMsg);
    }
    return store(key, value);
  }

  public ValidationContext<T> validateGroup(
      String groupName, Consumer<ValidationContext<T>> group) {
    if (!failed) {
      log.info("校验组[{}]", groupName);
      group.accept(this);
    }
    return this;
  }

  /**
   * 标记校验失败
   *
   * @param errorMsg 错误信息
   * @return 当前上下文（链式调用）
   */
  public ValidationContext<T> fail(String errorMsg) {
    if (!failed) {
      this.failed = true;
      this.errorMessage = errorMsg;
    }
    return this;
  }

  /**
   * 检查是否校验失败
   *
   * @return 失败状态
   */
  public boolean hasFailed() {
    return failed;
  }

  /**
   * 获取错误信息
   *
   * @return 错误信息（如果存在）
   */
  public Optional<String> getError() {
    return Optional.ofNullable(errorMessage);
  }

  /**
   * 获取目标对象
   *
   * @return 被校验的对象
   */
  public T getTarget() {
    return target;
  }

  /**
   * 完成校验并返回结果
   *
   * @return 校验结果
   */
  public ValidationResult finish() {
    return failed ? ValidationResult.invalid(errorMessage) : ValidationResult.valid();
  }

  // 构建验证结果（带业务上下文）
  public <C> ValidationRc<C> buildResult(Function<ValidationContext<T>, C> contextMapper) {
    if (errorMessage != null) {
      return ValidationRc.fail(errorMessage);
    }

    // 将中间数据转换为业务上下文
    C contextData = contextMapper.apply(this);
    return ValidationRc.success(contextData, new HashMap<>(intermediateData));
  }
}
