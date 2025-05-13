package com.ynthm.autoconfigure.excel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.ynthm.autoconfigure.excel.domain.*;
import com.ynthm.common.domain.Result;
import com.ynthm.common.domain.page.PageReq;
import com.ynthm.common.domain.page.PageResp;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.exception.BaseException;
import com.ynthm.common.web.core.util.ServletUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ethan Wang
 */
@Slf4j
public class ExcelHelper {

  private final ObjectMapper objectMapper;

  public ExcelHelper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T extends Serializable, P extends Serializable> void export1(
      HttpServletResponse response,
      String fileName,
      ExportExtraData<T> extraData,
      P param,
      Function<PageReq<P>, Result<PageResp<T>>> function) {
    try {

      PageReq<P> pageReq = new PageReq<>();
      pageReq.setPage(1).setSize(0).setParam(param);
      Result<PageResp<T>> countR = function.apply(pageReq);
      if (!countR.success()) {
        ServletUtil.renderString(response, objectMapper.writeValueAsString(countR));
        return;
      }

      if (countR.getData().getTotal() == 0) {
        ServletUtil.renderString(
            response,
            objectMapper.writeValueAsString(Result.error(BaseResultCode.NONE_DATA_EXPORT)));
        return;
      }

      String tempFileName = fileName + "_" + Instant.now().getEpochSecond();
      ServletUtil.responseForExcel(response, tempFileName);

      ExcelUtil excelUtil = new ExcelUtil();
      //      excelUtil.export(extraData, response.getOutputStream(), param, function);

    } catch (Exception e) {
      // 重置 response
      log.error("导出 Excel 失败", e);
      response.reset();
      try {
        ServletUtil.renderString(
            response,
            objectMapper.writeValueAsString(
                Result.error(BaseResultCode.EXCEL_EXPORT_FAILED, e.getMessage())));
      } catch (IOException ex) {
        throw new BaseException(ex);
      }
    }
  }

  /**
   * 批量导出
   *
   * @param response HttpServletResponse
   * @param fileName 文件名
   * @param extraData sheet 名
   * @param param 参数
   * @param function 分页逻辑
   * @param <T> 结果类型
   * @param <P> 参数类型
   */
  public <T extends Serializable, P extends Serializable> void export(
      HttpServletResponse response,
      String fileName,
      ExportExtraData<T> extraData,
      P param,
      Function<PageReq<P>, PageResp<T>> function) {
    try {
      String tempFileName = fileName + "_" + Instant.now().getEpochSecond();
      ServletUtil.responseForExcel(response, tempFileName);

      ExcelUtil excelUtil = new ExcelUtil();
      excelUtil.export(extraData, response.getOutputStream(), param, function);
    } catch (Exception e) {
      // 重置 response
      log.error("导出 Excel 失败", e);
      response.reset();
      try {
        ServletUtil.renderString(
            response,
            objectMapper.writeValueAsString(
                Result.error(BaseResultCode.EXCEL_EXPORT_FAILED, e.getMessage())));
      } catch (IOException ex) {
        throw new BaseException(ex);
      }
    }
  }

  public <P extends Serializable, T extends Serializable> void fill(
      HttpServletResponse response,
      String fileName,
      ExportExtraData<T> extraData,
      InputStream template,
      P param,
      Function<PageReq<P>, List<T>> function) {

    try {
      String tempFileName = fileName + "_" + Instant.now().getEpochSecond();
      ServletUtil.responseForExcel(response, tempFileName);

      ExcelUtil excelUtil = new ExcelUtil();
      excelUtil.fill(extraData, template, response.getOutputStream(), param, function);
    } catch (Exception e) {
      // 重置 response
      log.error("导出 Excel 失败", e);
      response.reset();
      throw new BaseException(BaseResultCode.EXCEL_EXPORT_FAILED, e);
    }
  }

  /**
   * 批量导出
   *
   * @param responseOutputStream OutputStream
   * @param fileNameForResponse {@link ServletUtil#responseForExcel}
   * @param fileName 文件名
   * @param param 参数
   * @param function 分页逻辑
   * @param <T> 结果类型
   * @param <P> 参数类型
   */
  public <T extends Serializable, P extends Serializable> Result<String> export(
      OutputStream responseOutputStream,
      Consumer<String> fileNameForResponse,
      String fileName,
      ExportExtraData<T> extraData,
      P param,
      Function<PageReq<P>, PageResp<T>> function) {
    try {
      String tempFileName = fileName + "_" + Instant.now().getEpochSecond();
      fileNameForResponse.accept(tempFileName);

      ExcelUtil excelUtil = new ExcelUtil();
      excelUtil.export(extraData, responseOutputStream, param, function);
      return Result.ok();
    } catch (Exception e) {
      // 重置 response
      throw new BaseException(e);
    }
  }

  /**
   * Excel 模板导入数据 如果数据校验失败返回附带错误信息的导入模板
   *
   * @param fileName 模板名字
   * @param inputStream 导入文件输入流
   * @param response 响应
   * @param head 行所对应的实体类型
   * @param function 处理 Sheet 页数据并放回校验错误(空代表没有错误)
   * @param errorColumnIndex 错误写入的列 index
   * @param <T> 数据行类型
   * @throws IOException IO 异常
   */
  public <T extends RowNum> void importDataIfErrorReport(
      String fileName,
      InputStream inputStream,
      HttpServletResponse response,
      Class<T> head,
      Function<List<T>, List<RowError>> function,
      int errorColumnIndex)
      throws IOException {
    ExcelUtil excelUtil = new ExcelUtil();
    List<RowError> errors =
        excelUtil.readSheet(inputStream, new ReadMayWriteErrorData<>(head), function);
    if (!errors.isEmpty()) {
      ServletUtil.responseForExcel(response, fileName + "_" + Instant.now().getEpochSecond());
      Map<Integer, List<RowError>> sheetIndex2Errors = Maps.newHashMap();
      sheetIndex2Errors.put(0, errors);
      excelUtil.writeToTemplate(
          inputStream, response.getOutputStream(), errorColumnIndex, sheetIndex2Errors);
    }
  }
}
