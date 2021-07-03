package com.ynthm.common.exception;

import com.ynthm.common.enums.ResultCode;
import lombok.Data;

/** @author ethan */
@Data
public class BaseException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private ResultCode resultCode;

  public BaseException(ResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }

  public BaseException(ResultCode resultCode, Throwable cause) {
    super(resultCode.getMessage(), cause);
    this.resultCode = resultCode;
  }
}
