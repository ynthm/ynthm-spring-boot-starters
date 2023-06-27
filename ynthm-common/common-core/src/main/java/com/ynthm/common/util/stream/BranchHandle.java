package com.ynthm.common.util.stream;

/**
 * 分支处理接口
 *
 * @author Ynthm Wang
 * @version 1.0
 */
@FunctionalInterface
public interface BranchHandle {

  /**
   * 分支操作
   *
   * @param trueHandle 为true时要进行的操作
   * @param falseHandle 为false时要进行的操作
   */
  void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}
