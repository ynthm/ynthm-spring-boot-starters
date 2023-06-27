package com.ynthm.common.domain;

import com.google.common.collect.Lists;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.enums.ResultCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 响应信息主体
 *
 * @author Ethan Wang
 */
@Data
@Accessors(chain = true)
public class Result<T> {

  public static final String FIELD_CODE = "code";
  public static final String FIELD_MESSAGE = "msg";
  public static final String FIELD_DATA = "data";

  private int code;

  private String msg;

  /**
   * 返回错误
   *
   * <p>当有第三方错误时，返回错误码
   */
  private T data;

  private List<ErrorItem> errors;

  public Result<T> addError(String code, String msg) {
    if (Objects.isNull(this.errors)) {
      this.errors = new ArrayList<>();
    }

    this.errors.add(new ErrorItem(code, msg));

    return this;
  }

  public static <T> Result<T> ok(T data) {
    return of(BaseResultCode.OK, data);
  }

  public static <T> Result<T> ok() {
    return of(BaseResultCode.OK, null);
  }

  public static <T> Result<T> warn(String message) {
    return ofMessage(BaseResultCode.WARN, message);
  }

  public static <T> Result<T> error(ResultCode errorCode) {
    return of(null, errorCode.getCode(), errorCode.getMessage(), null);
  }

  public static <T> Result<T> error(ResultCode errorCode, String message) {
    return ofMessage(errorCode, message);
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

  public static <T> Result<T> of(ResultCode resultCode, T data) {
    return of(data, resultCode.getCode(), resultCode.getMessage(), null);
  }

  public static <T> Result<T> ofMessage(ResultCode resultCode, String message) {
    return of(null, resultCode.getCode(), message, null);
  }

  public static <T> Result<T> of(T data, int code, String msg) {
    return of(data, code, msg, null);
  }

  public static <T> Result<T> of(T data, int code, String msg, List<ErrorItem> errors) {
    Result<T> apiResult = new Result<>();
    apiResult.setCode(code);
    apiResult.setMsg(msg);
    apiResult.setData(data);
    apiResult.setErrors(errors);
    return apiResult;
  }

  public boolean success() {
    return this.code == BaseResultCode.OK.getCode();
  }

  /**
   * 微服务内部错误转换
   *
   * @param result
   * @return
   * @param <T>
   */
  public static <T> Result<T> internalResult(Result<T> result) {
    if (result.success()) {
      return Result.ok(result.getData());
    } else {
      List<ErrorItem> errorItems =
          Lists.newArrayList(new ErrorItem(String.valueOf(result.getCode()), result.getMsg()));
      if (Objects.nonNull(result.getErrors())) {
        errorItems.addAll(result.getErrors());
      }
      return Result.of(
          null,
          BaseResultCode.INTERNAL_ERROR.getCode(),
          BaseResultCode.INTERNAL_ERROR.getMessage(),
          errorItems);
    }
  }

  public static <T, S> Result<T> thenApply(Result<S> result, Function<S, Result<T>> function) {
    if (result.success()) {
      return function.apply(result.getData());
    } else {
      return transferMassage(result);
    }
  }

  public static <S> Result<Void> thenRun(Result<S> result, Runnable runnable) {
    if (result.success()) {
      runnable.run();
      return Result.ok();
    } else {
      return transferMassage(result);
    }
  }

  private static <T, S> Result<T> transferMassage(Result<S> result) {
    return Result.of(null, result.getCode(), result.getMsg(), result.getErrors());
  }
}
