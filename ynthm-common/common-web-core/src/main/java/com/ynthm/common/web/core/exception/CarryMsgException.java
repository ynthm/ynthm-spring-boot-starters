package com.ynthm.common.web.core.exception;


import com.ynthm.common.domain.KeyParamPair;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.exception.BaseException;

/**
 * @author Ethan Wang
 */
public class CarryMsgException extends BaseException {

  public CarryMsgException(ResultCode resultCode, String message, KeyParamPair args) {
    super(message + args.string(), resultCode);
  }

  public CarryMsgException(ResultCode resultCode, String message) {
    super(message, resultCode);
  }

  public static void throwIfNull(Object object, ResultCode resultCode, String message, KeyParamPair args) {
    if (object == null) {
      throw new CarryMsgException(resultCode, message, args);
    }
  }

  public static void throwIfNotInDb(Object object, String message, KeyParamPair args) {
    if (object == null) {
      throw new CarryMsgException(BaseResultCode.DB_NOT_EXIST, message, args);
    }
  }
}

