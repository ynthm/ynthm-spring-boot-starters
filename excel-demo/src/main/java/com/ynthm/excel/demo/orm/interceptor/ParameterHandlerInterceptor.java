package com.ynthm.excel.demo.orm.interceptor;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Component
@Intercepts({
  @Signature(
      type = ParameterHandler.class,
      method = "setParameters",
      args = {PreparedStatement.class})
})
public class ParameterHandlerInterceptor implements Interceptor {
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}
}
