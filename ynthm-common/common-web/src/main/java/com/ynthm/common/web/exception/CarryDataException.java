package com.ynthm.common.web.exception;

import com.ynthm.common.domain.Result;

/**
 * @author Ethan Wang
 */
public class CarryDataException extends RuntimeException {
  private final Result<String> result;

  public CarryDataException(Result<String> result) {
    super(result.getMsg());
    this.result = result;
  }

  public CarryDataException(Result<String> result, Throwable cause) {
    super(result.getMsg(), cause);
    this.result = result;
  }

  public Result<String> getResult() {
    return result;
  }
}
