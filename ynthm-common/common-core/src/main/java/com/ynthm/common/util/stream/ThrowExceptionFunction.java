package com.ynthm.common.util.stream;

/**
 * 抛异常接口
 *
 * @author Ynthm Wang
 * @version 1.0
 */
@FunctionalInterface
public interface ThrowExceptionFunction {

  /**
   * 抛出异常信息
   *
   * @param message 异常信息
   */
  void throwMessage(String message);
}
