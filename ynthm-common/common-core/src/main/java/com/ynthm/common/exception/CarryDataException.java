package com.ynthm.common.exception;

import com.ynthm.common.domain.Result;

/**
 * @author Ethan Wang
 */
public class CarryDataException extends RuntimeException {
  private final Result<Void> result;

  public CarryDataException(Result<Void> result) {
    super(result.getMsg());
    this.result = result;
  }

  public CarryDataException(Result<Void> result, Throwable cause) {
    super(result.getMsg(), cause);
    this.result = result;
  }

  public Result<Void> getResult() {
    return result;
  }
}
