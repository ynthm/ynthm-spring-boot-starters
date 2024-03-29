package com.ynthm.common.excel.enums;

import com.ynthm.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ethan Wang
 */
@Getter
@AllArgsConstructor
public enum ResultCodeExcel implements ResultCode {

  /** 模板导出失败 */
  EXCEL_EXPORT_FAILED(31001, "模板导出失败"),

  RECORD_EXIST(31002, "数据库已经存在记录");

  /** 返回码 */
  private final int code;
  /** 返回消息 */
  private final String message;
}
