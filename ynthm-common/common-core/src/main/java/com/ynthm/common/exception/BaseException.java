package com.ynthm.common.exception;

import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.enums.ResultCode;

/**
 * @author Ethan Wang
 */
public class BaseException extends RuntimeException {
  protected final ResultCode resultCode;

  public BaseException() {
    super();
    this.resultCode = BaseResultCode.ERROR;
  }

  public BaseException(String message) {
    super(message);
    this.resultCode = BaseResultCode.ERROR;
  }

  public BaseException(ResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }

  public BaseException(ResultCode resultCode, Throwable cause) {
    super(resultCode.getMessage(), cause);
    this.resultCode = resultCode;
  }

  public BaseException(Throwable cause) {
    super(cause);
    this.resultCode = BaseResultCode.ERROR;
  }

  public ResultCode getResultCode() {
    return resultCode;
  }
}
