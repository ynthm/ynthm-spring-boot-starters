package com.ynthm.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/** @author ethan */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
  private static final long serialVersionUID = 1L;

  private int code;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public static <T> Result<T> ok(T data) {
    return restResult(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), data);
  }

  public static <T> Result<T> ok() {
    return restResult(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), null);
  }

  public static <T> Result<T> warn(String message) {
    return restResult(ResultCode.WARN.getCode(), message, null);
  }

  public static <T> Result<T> error(IResultCode errorCode) {
    return restResult(errorCode.getCode(), errorCode.getMessage(), null);
  }

  public static <T> Result<T> error(IResultCode errorCode, String message) {
    return restResult(errorCode.getCode(), message, null);
  }

  private static <T> Result<T> restResult(int code, String msg, T data) {
    Result<T> apiResult = new Result<>();
    apiResult.setCode(code);
    apiResult.setData(data);
    apiResult.setMessage(msg);
    return apiResult;
  }

  public String toJson(ObjectMapper om) throws JsonProcessingException {
    return om.writeValueAsString(this);
  }

  public boolean success() {
    return ResultCode.OK.getCode() == code;
  }
}
