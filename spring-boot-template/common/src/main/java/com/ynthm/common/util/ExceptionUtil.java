package com.ynthm.common.util;

import com.ynthm.common.Result;
import com.ynthm.common.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/** @author ethan */
@Slf4j
public class ExceptionUtil {

  private ExceptionUtil() {}

  /** 打印异常信息 */
  public static String getPrintStackTrace(Throwable e) {
    String swStr = null;
    try (StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw)) {
      e.printStackTrace(pw);
      pw.flush();
      sw.flush();
      swStr = sw.toString();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }
    return swStr;
  }

  public static String getFullStackTrace(Exception e) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos)) {
      e.printStackTrace(ps);
      return bos.toString();
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return null;
  }

  public static Result<String> exResult(Exception e) {
    return Result.error(ResultCode.FAILED, getFullStackTrace(e));
  }

  public static Result<String> exThrowableResult(Throwable e) {
    return Result.error(ResultCode.FAILED, getPrintStackTrace(e));
  }
}
