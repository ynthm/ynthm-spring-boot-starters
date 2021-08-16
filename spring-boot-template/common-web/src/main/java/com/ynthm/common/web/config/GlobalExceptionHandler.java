package com.ynthm.common.web.config;

import cn.hutool.core.util.StrUtil;
import com.ynthm.common.Result;
import com.ynthm.common.constant.Constant;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.exception.BaseException;
import com.ynthm.common.util.ExceptionUtil;
import com.ynthm.common.web.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;

/**
 * 所有请求返回 200 用错误码表示
 *
 * <p>如果要遵循 Restful 可使用 ResponseEntity<T> 并无优劣
 *
 * @author ethan
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  public static final String MESSAGE_CODE_PREFIX = "response.";
  /** 当前环境 */
  @Value("${spring.profiles.active}")
  private String profile;

  @Resource private I18nUtil i18nUtil;

  /**
   * 自定义异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = BaseException.class)
  @ResponseBody
  public Result<String> handleBaseException(BaseException e) {
    log.info(ExceptionUtil.getPrintStackTrace(e));

    if (Constant.ENV_PROD.equals(profile)) {
      // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
      String message = getMessage(e);
      return Result.error(e.getResultCode(), message);
    }

    return Result.error(e.getResultCode(), ExceptionUtil.getFullStackTrace(e));
  }

  /**
   * Controller上一层相关异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler({
    NoHandlerFoundException.class,
    HttpRequestMethodNotSupportedException.class,
    HttpMediaTypeNotSupportedException.class,
    MissingPathVariableException.class,
    MissingServletRequestParameterException.class,
    TypeMismatchException.class,
    HttpMessageNotReadableException.class,
    HttpMessageNotWritableException.class,
    HttpMediaTypeNotAcceptableException.class,
    ServletRequestBindingException.class,
    ConversionNotSupportedException.class,
    MissingServletRequestPartException.class,
    AsyncRequestTimeoutException.class
  })
  @ResponseBody
  public Result<String> handleServletException(Exception e) {
    log.error(e.getMessage(), e);
    ResultCode resultCode = ResultCode.SERVER_ERROR;
    try {
      resultCode = ResultCode.valueOf(e.getClass().getSimpleName());
    } catch (IllegalArgumentException e1) {
      log.error("class [{}] not defined in ResultCode.", e.getClass().getName());
    }

    return handleException(new BaseException(resultCode));
  }

  /**
   * 参数绑定异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = BindException.class)
  @ResponseBody
  public Result<String> handleBindException(BindException e) {
    log.error("参数绑定校验异常", e);

    return wrapperBindingResult(e.getBindingResult());
  }

  /**
   * 参数校验异常，将校验失败的所有异常组合成一条错误信息
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseBody
  public Result<String> handleValidException(MethodArgumentNotValidException e) {
    log.error("参数绑定校验异常", e);

    return wrapperBindingResult(e.getBindingResult());
  }

  /**
   * 包装绑定异常结果
   *
   * @param bindingResult 绑定结果
   * @return 异常结果
   */
  private Result<String> wrapperBindingResult(BindingResult bindingResult) {
    StringBuilder msg = new StringBuilder();

    for (ObjectError error : bindingResult.getAllErrors()) {
      msg.append(", ");
      if (error instanceof FieldError) {
        msg.append(((FieldError) error).getField()).append(": ");
      }
      msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
    }

    return Result.error(ResultCode.VALID_ERROR, msg.substring(2));
  }

  /**
   * 未定义异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public Result<String> handleException(Exception e) {
    log.info(ExceptionUtil.getPrintStackTrace(e));

    return handleBaseException(new BaseException(ResultCode.SERVER_ERROR, e));
  }

  /**
   * 获取国际化消息
   *
   * @param e 异常
   * @return
   */
  private String getMessage(BaseException e) {
    String code = MESSAGE_CODE_PREFIX + e.getResultCode().getCode();
    String message = i18nUtil.getMessage(code);

    if (StrUtil.isBlank(message)) {
      return ExceptionUtil.getPrintStackTrace(e);
    }
    return message;
  }
}
