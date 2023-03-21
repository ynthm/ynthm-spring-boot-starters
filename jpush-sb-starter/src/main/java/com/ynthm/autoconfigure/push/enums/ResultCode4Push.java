package com.ynthm.autoconfigure.push.enums;

import com.ynthm.common.enums.ResultCode;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public enum ResultCode4Push implements ResultCode {
  SEND_ERROR(90100, "极光推送错误"),
  ;

  private final int code;
  private final String message;

  private ResultCode4Push(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
