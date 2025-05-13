package com.ynthm.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.enums.ResultCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

/**
 * 响应信息主体
 *
 * @author Ethan Wang
 */
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
public class Result<T> {

  public static final String FIELD_CODE = "code";
  public static final String FIELD_MESSAGE = "msg";
  public static final String FIELD_DATA = "data";
  public static final String FIELD_ERRORS = "errors";

  private int code;

  private String msg;

  /**
   * 返回错误
   *
   * <p>当有第三方错误时，返回错误码
   */
  private T data;

  private List<ErrorItem> errors;

  public static <T> Result<T> ok(T data) {
    Result<T> apiResult = new Result<>();
    apiResult.setCode(BaseResultCode.OK.getCode());
    apiResult.setMsg(BaseResultCode.OK.getMessage());
    apiResult.setData(data);
    return apiResult;
  }

  public static <T> Result<T> ok() {
    return ok(null);
  }

  public static <T> Result<T> warn(String message) {
    return error(BaseResultCode.WARN.getCode(), message);
  }

  public static <T> Result<T> error(ResultCode errorCode) {
    return error(errorCode.getCode(), errorCode.getMessage(), null);
  }

  /** 错误不需要抛出异常，直接返回并打印日志，记录后台上下文参数 */
  public static <T> Result<T> errorReturnAndLog(
      ResultCode resultCode, String format, Object... args) {

    log.error(
        "[{}] {}",
        resultCode.getMessage(),
        MessageFormatter.arrayFormat(format, args).getMessage());
    return error(resultCode);
  }

  /**
   * 外部错误(包括二方三方错误)
   *
   * <p>多错误通过 addError 追加
   *
   * @param resultCode 内部错误码
   * @param errorCode 外部错误码
   * @param errorMessage 外部错误消息
   * @return 结果
   * @param <T> 结果类型
   */
  public static <T> Result<T> error(ResultCode resultCode, String errorCode, String errorMessage) {
    return Result.<T>error(resultCode).addError(errorCode, errorMessage);
  }

  public static <T> Result<T> error(ResultCode code, String message) {
    return error(code.getCode(), message, null);
  }

  public static <T> Result<T> error(int code, String message) {
    return error(code, message, null);
  }

  public static <T> Result<T> error(int code, String msg, List<ErrorItem> errors) {
    Result<T> apiResult = new Result<>();
    apiResult.setCode(code);
    apiResult.setMsg(msg);
    apiResult.setErrors(errors);
    return apiResult;
  }

  /**
   * 一般用在 Controller 层手动校验
   *
   * @param vo 前端传递 VO
   * @param errorFunction 校验 VO 返回 Optional<ResultCode>
   * @param passed 通过校验后执行逻辑
   * @param <V> VO 类型
   * @param <T> 返回类型
   */
  public static <V, T> Result<T> validateThenApply(
      V vo, Function<V, Optional<ResultCode>> errorFunction, Function<V, Result<T>> passed) {
    return errorFunction.apply(vo).<Result<T>>map(Result::error).orElseGet(() -> passed.apply(vo));
  }

  public Result<T> addError(String code, String msg) {
    if (Objects.isNull(this.errors)) {
      this.errors = new ArrayList<>();
    }

    this.errors.add(new ErrorItem(code, msg));

    return this;
  }

  public boolean success() {
    return this.code == BaseResultCode.OK.getCode();
  }

  public <S> Result<S> transferMassage() {
    return Result.error(getCode(), getMsg(), getErrors());
  }

  public <S> Result<S> thenApply(Function<T, Result<S>> function) {
    if (success()) {
      return function.apply(getData());
    } else {
      return transferMassage();
    }
  }

  public Result<Void> thenRun(Runnable runnable) {
    if (success()) {
      runnable.run();
      return Result.ok();
    } else {
      return transferMassage();
    }
  }
}
