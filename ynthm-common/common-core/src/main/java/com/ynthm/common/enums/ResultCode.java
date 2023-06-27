package com.ynthm.common.enums;

/**
 * 结果错误码接口
 *
 * @author Ethan Wang
 */
public interface ResultCode {
  /**
   * 业务状态码
   *
   * @return 编码
   */
  int getCode();

  /**
   * 业务状态消息
   *
   * @return 消息
   */
  String getMessage();
}
