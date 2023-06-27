package com.ynthm.common.web.config;

import com.ynthm.common.domain.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 状态码 200~299 的请求直接放回数据，其他的返回 {@link Result}
 *
 * <p>加上 @ControllerAdvice 注解 注册到 RequestMappingHandlerAdapter
 *
 * @author Ethan Wang
 * @version 1.0
 */
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof Result) {
      Result result = ((Result<?>) body);
      if (result.success()) {
        return result.getData();
      }
    }
    return body;
  }
}
