package com.ynthm.common.exception;

import com.ynthm.common.IResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** @author ethan */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException  {
  protected final IResultCode resultCode;

  public BaseException(IResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }

  public BaseException(IResultCode resultCode, Throwable cause) {
    super(resultCode.getMessage(), cause);
    this.resultCode = resultCode;
  }
}
