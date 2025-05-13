package com.ynthm.common.web.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.ynthm.common.domain.BindError;
import com.ynthm.common.domain.ErrorItem;
import com.ynthm.common.domain.Result;
import com.ynthm.common.enums.BaseResultCode;
import com.ynthm.common.exception.BaseException;
import com.ynthm.common.exception.CarryDataException;
import com.ynthm.common.util.ExceptionUtil;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.WebUtils;

/**
 * 全局异常处理
 *
 * @author Ynthm Wang
 */
@Slf4j
@RestControllerAdvice
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class GlobalExceptionHandler extends BasicErrorController {

  public static final String MESSAGE = "message";
  private ObjectMapper objectMapper;

  /** 引用父类构造方法，将设置的ErrorProperties传入父类 */
  public GlobalExceptionHandler() {
    super(new DefaultErrorAttributes(), initProperties());
  }

  /** 设置ErrorProperties参数值 */
  public static ErrorProperties initProperties() {
    ErrorProperties properties = new ErrorProperties();
    properties.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
    return properties;
  }

  @Nullable
  private static HttpStatus getHttpStatus(HttpServletRequest request) {
    HttpStatus httpStatus;
    httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
    if (request instanceof ServletWebRequest) {
      ServletWebRequest servletWebRequest = (ServletWebRequest) request;
      HttpServletResponse response = servletWebRequest.getResponse();
      if (response != null && response.isCommitted()) {
        log.warn("Async request timed out");
        return null;
      }
    }
    return httpStatus;
  }

  @NotNull
  private static HttpStatus getHttpStatus(
      HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpHeaders headers) {
    HttpStatus httpStatus;
    httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
    if (!CollectionUtils.isEmpty(mediaTypes)) {
      headers.setAccept(mediaTypes);
      if (request instanceof ServletWebRequest) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        if (HttpMethod.PATCH.equals(servletWebRequest.getHttpMethod())) {
          headers.setAcceptPatch(mediaTypes);
        }
      }
    }
    return httpStatus;
  }

  @Resource
  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * 自定义异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = BaseException.class)
  public Result<Boolean> handleBaseException(BaseException e, HttpServletRequest request) {
    log.error(
        MoreObjects.toStringHelper("error")
            .add("url", request.getRequestURI())
            .add(MESSAGE, e.getMessage())
            .toString(),
        e);

    return Result.error(e.getResultCode(), getMessage(e));
  }

  @ExceptionHandler(value = CarryDataException.class)
  public Result<Void> handleBaseException(CarryDataException e) {
    log.error(e.getLocalizedMessage(), e);

    return e.getResult();
  }

  @ExceptionHandler(value = NestedRuntimeException.class)
  public Result<Boolean> nestedRuntimeException(NestedRuntimeException e) {
    log.error(e.getMessage(), e);

    return Result.error(BaseResultCode.ERROR, e.getMostSpecificCause().getLocalizedMessage());
  }

  private String getMessage(Exception e) {
    StringBuilder sb = new StringBuilder();
    sb.append(e.getLocalizedMessage());
    Throwable cause = e.getCause();
    while (cause != null) {
      sb.append("\n").append(cause.getLocalizedMessage());
      cause = cause.getCause();
    }

    return sb.toString();
  }

  /**
   * Controller 之前发生异常
   *
   * @param request 请求
   * @return 自定义的返回实体类
   */
  @Override
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    Map<String, Object> result = new HashMap<>(3);
    if (status == HttpStatus.NO_CONTENT) {
      result.put(Result.FIELD_CODE, status.value());
      result.put(Result.FIELD_MESSAGE, status.getReasonPhrase());
      return new ResponseEntity<>(result, status);
    }
    Map<String, Object> body =
        getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
    log.error("controller before exception: {}; ", body);
    Object message = body.remove(MESSAGE);
    body.remove("status");
    result.put(Result.FIELD_CODE, status.value());
    result.put(Result.FIELD_MESSAGE, message.toString());
    result.put(
        Result.FIELD_ERRORS,
        body.entrySet().stream()
            .map(i -> new ErrorItem(i.getKey(), i.getValue().toString()))
            .collect(Collectors.toList()));

    return ResponseEntity.ok().body(result);
  }

  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    // 404 等无法异常捕捉的异常页面转内部异常
    HttpStatus status = this.getStatus(request);
    Map<String, Object> body =
        this.getErrorAttributes(
            request, this.getErrorAttributeOptions(request, MediaType.TEXT_HTML));
    log.error("controller before exception: {}; ", body);
    Object message = body.get(MESSAGE);

    String jsonBody;
    try {
      jsonBody = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      throw new BaseException(e);
    }

    throw new CarryDataException(
        Result.error(
            status.value(),
            message.toString(),
            Lists.newArrayList(new ErrorItem(String.valueOf(status.value()), jsonBody))));
  }

  @SneakyThrows
  @Override
  @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
  public ResponseEntity<String> mediaTypeNotAcceptable(HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

    return ResponseEntity.ok(
        objectMapper.writeValueAsString(
            Result.error(status.value(), status.getReasonPhrase(), null)));
  }

  /**
   * Controller上一层相关异常
   *
   * @param ex 异常
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
    ServletRequestBindingException.class,
    ConversionNotSupportedException.class,
    MissingServletRequestPartException.class,
    AsyncRequestTimeoutException.class
  })
  @ResponseBody
  public ResponseEntity<Result<String>> handleServletException(
      Exception ex, HttpServletRequest request) {
    log.error(ex.getMessage(), ex);

    Map<String, Object> body =
        this.getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
    log.error("exception: {}; ", body);

    String message = body.get(MESSAGE).toString();

    Result<String> result;
    HttpHeaders headers = new HttpHeaders();

    // MissingServletRequestParameterException ServletRequestBindingException TypeMismatchException
    // HttpMessageNotReadableException MethodArgumentNotValidException
    // MissingServletRequestPartException
    // BindException
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    if (ex instanceof HttpRequestMethodNotSupportedException) {
      HttpRequestMethodNotSupportedException e = (HttpRequestMethodNotSupportedException) ex;
      httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
      Set<HttpMethod> supportedMethods = e.getSupportedHttpMethods();

      if (!CollectionUtils.isEmpty(supportedMethods)) {
        headers.setAllow(supportedMethods);
      }

    } else if (ex instanceof HttpMediaTypeNotSupportedException) {
      httpStatus = getHttpStatus((HttpMediaTypeNotSupportedException) ex, request, headers);
    } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
      httpStatus = HttpStatus.NOT_ACCEPTABLE;

    } else if (ex instanceof MissingPathVariableException) {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    } else if (ex instanceof ConversionNotSupportedException) {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    } else if (ex instanceof HttpMessageNotWritableException) {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    } else if (ex instanceof NoHandlerFoundException) {
      httpStatus = HttpStatus.NOT_FOUND;

    } else if (ex instanceof AsyncRequestTimeoutException) {
      httpStatus = getHttpStatus(request);
      if (httpStatus == null) {
        return null;
      }
    }

    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex);
    }

    result = Result.error(httpStatus.value(), message);

    return ResponseEntity.ok().headers(headers).body(result);
  }

  /**
   * 参数校验异常，将校验失败的所有异常组合成一条错误信息
   *
   * <p>POST MethodArgumentNotValidException
   *
   * <p>GET BindException
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
  public Result<ArrayList<BindError>> handleValidException(MethodArgumentNotValidException e) {
    log.error(e.getLocalizedMessage(), e);
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return Result.error(
        status.value(), status.getReasonPhrase(), wrapperBindingResult(e.getBindingResult()));
  }

  /**
   * jsr 规范中的验证异常，嵌套检验问题
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public Result<String> constraintViolationException(ConstraintViolationException e) {
    log.error(e.getLocalizedMessage(), e);
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    String message =
        violations.stream()
            .map(item -> String.join(": ", item.getPropertyPath().toString(), item.getMessage()))
            .collect(Collectors.joining(", "));
    return Result.error(BaseResultCode.VALID_ERROR, message);
  }

  /**
   * 包装绑定异常结果
   *
   * @param bindingResult 绑定结果
   * @return 异常结果
   */
  private List<ErrorItem> wrapperBindingResult(BindingResult bindingResult) {
    return bindingResult.getAllErrors().stream()
        .map(
            error -> {
              if (error instanceof FieldError) {
                return new BindError(
                    error.getCode(), error.getDefaultMessage(), ((FieldError) error).getField());
              } else {
                return new ErrorItem(error.getCode(), error.getDefaultMessage());
              }
            })
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * 未定义异常
   *
   * @param e 异常
   * @return 异常结果
   */
  @ExceptionHandler(value = Exception.class)
  public Result<Void> handleException(Exception e) {
    if (e instanceof JacksonException) {
      return Result.error(BaseResultCode.ERROR, ((JacksonException) e).getOriginalMessage());
    } else if (e instanceof ValidationException) {
      return Result.error(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage(), null);
    }
    log.error(ExceptionUtil.printStackTrace(e));
    return Result.error(BaseResultCode.ERROR, getMessage(e));
  }
}
