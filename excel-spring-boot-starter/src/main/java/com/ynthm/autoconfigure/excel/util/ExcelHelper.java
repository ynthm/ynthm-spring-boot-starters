package com.ynthm.autoconfigure.excel.util;

import com.ynthm.common.domain.PageReq;
import com.ynthm.common.domain.PageResp;
import com.ynthm.common.domain.Result;
import com.ynthm.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ethan Wang
 */
@Slf4j
public class ExcelHelper {

  /**
   * 批量导出
   *
   * @param responseOutputStream OutputStream
   * @param fileNameForResponse {@link ServletUtil#responseForExcel}
   * @param type 含有 @ExcelProperty 注解的VO实体
   * @param fileName 文件名
   * @param sheetName sheet 名
   * @param param 参数
   * @param function 分页逻辑
   * @param <T> 结果类型
   * @param <P> 参数类型
   */
  public <T extends Serializable, P extends Serializable> Result<String> export(
      OutputStream responseOutputStream,
      Consumer<String> fileNameForResponse,
      Class<T> type,
      String fileName,
      String sheetName,
      P param,
      Function<PageReq<P>, PageResp<T>> function) {
    try {
      String tempFileName = fileName + "_" + Instant.now().getEpochSecond();
      fileNameForResponse.accept(tempFileName);

      ExcelUtil<T> excelUtil = new ExcelUtil<>(type);
      excelUtil.export(sheetName, responseOutputStream, param, function);
      return Result.ok();
    } catch (Exception e) {
      // 重置 response
      throw new BaseException(e);
    }
  }
}
