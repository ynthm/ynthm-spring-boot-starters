package com.ynthm.common.exception;

import com.ynthm.common.enums.ResultCode;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class UtilException extends BaseException {
  public UtilException(ResultCode resultCode) {
    super(resultCode);
  }

  public UtilException(ResultCode resultCode, Throwable cause) {
    super(resultCode, cause);
  }

  public UtilException(Throwable cause) {
    super(cause);
  }
}
