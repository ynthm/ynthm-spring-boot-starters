package com.ynthm.common.enums;

import com.ynthm.common.IResultCode;
import com.ynthm.common.ResultExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author Ethan Wang */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode, ResultExceptionAssert {
  /** 失败 */
  FAILED(-1, "FAILED"),
  /** 成功 */
  OK(0, "SUCCESS"),
  /** 警告 */
  WARN(1, "WARN"),

  /** 用户输入 */
  VALID_ERROR(20000, "参数校验错误"),
  EMPTY_PARAMETER_ERROR(20001, "未输入必要参数"),
  LOGIN_ACCOUNT_BLANK(20002, "请确认输入正确的登录账号"),
  VERIFICATION_CODE_ERROR(20004, "验证码匹配失败"),
  USERNAME_NOT_EXIST(20005, "账号不存在"),
  USERNAME_EXIST(20006, "账号已存在"),

  /** 数据库不存在记录 */
  RECORD_NOT_EXIST(21001, "数据库不存在记录"),
  /** 数据库已经存在记录 */
  RECORD_EXIST(21002, "数据库已经存在记录"),

  RECORD_UNAVAILABLE(21003, "数据库记录不可用"),

  SERVER_ERROR(50000, "系统错误"),

  UNKNOWN_ERROR(99999, "Unknown Error!");

  /** 返回码 */
  private final int code;
  /** 返回消息 */
  private final String message;
}
