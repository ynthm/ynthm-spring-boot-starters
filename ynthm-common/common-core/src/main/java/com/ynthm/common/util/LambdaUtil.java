package com.ynthm.common.util;

import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.exception.BaseException;
import com.ynthm.common.util.stream.BranchHandle;
import com.ynthm.common.util.stream.ThrowExceptionFunction;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class LambdaUtil {

  private LambdaUtil() {}

  /**
   * 如果参数为true抛出异常
   *
   * @param b
   * @return com.example.demo.func.ThrowExceptionFunction
   */
  public static ThrowExceptionFunction isTure(boolean b) {
    return errorMessage -> {
      if (b) {
        throw new RuntimeException(errorMessage);
      }
    };
  }

  public static void ifTrueThrow(boolean error, ResultCode resultCode) {
    if (error) {
      throw new BaseException(resultCode);
    }
  }

  /**
   * 参数为true或false时，分别进行不同的操作
   *
   * @param b
   * @return com.example.demo.func.BranchHandle
   */
  public static BranchHandle isTureOrFalse(boolean b) {

    return (trueHandle, falseHandle) -> {
      if (b) {
        trueHandle.run();
      } else {
        falseHandle.run();
      }
    };
  }
}
