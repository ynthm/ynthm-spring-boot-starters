package com.ynthm.common.enums;

import com.ynthm.common.exception.ResultExceptionAssert;

/**
 * 公共错误码
 *
 * <p>路径区分业务模块
 *
 * @author Ethan Wang
 */
public enum BaseResultCode implements ResultCode, ResultExceptionAssert {
  /** OK */
  OK(0, "OK"),
  ERROR(-1, "ERROR"),
  WARN(1, "WARN"),

  NULL(998, "系统错误 NULL"),
  UNKNOWN_ERROR(999, "Unknown Error!"),

  /** 权限部分 */
  AUTHENTICATION_EXCEPTION(1000, "权限异常"),
  SIGN_VERIFY_FAILED(1001, "签名验证失败"),
  UNAUTHORIZED(1002, "未登录，请先登录。"),
  BAD_CREDENTIALS(1003, "用户名或密码错误，请确认。"),
  WRONG_PASSWORD(1004, "密码错误，请确认。"),
  USERNAME_NOT_FOUND(1005, "用户名不存在。"),

  /** 用户输入 */
  VALID_ERROR(2000, "参数校验错误"),
  DB_NOT_EXIST(2001, "数据库不存在"),
  DB_EXIST(2002, "数据库已存在"),

  EXCEL_EXPORT_FAILED(10000, "Excel 导出异常"),

  INTERNAL_ERROR(90000, "微服务内部错误"),
  /** 调用第三方服务发生错误 */
  THIRD_PART_SERVICE_ERROR(90001, "调用第三方服务发生错误"),

  /** 每个服务 10000个编码 */
  S_AUTH_ERROR(100000, "AUTH服务错误"),
  ;

  private final int code;
  private final String message;

  BaseResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
