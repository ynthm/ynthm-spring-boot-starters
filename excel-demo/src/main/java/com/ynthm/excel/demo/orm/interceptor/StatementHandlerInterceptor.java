package com.ynthm.excel.demo.orm.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
@Component
@Intercepts({
  @Signature(
      type = StatementHandler.class,
      method = "update",
      args = {Statement.class})
})
public class StatementHandlerInterceptor implements Interceptor {
  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    BoundSql boundSql = statementHandler.getBoundSql();
    String sql = boundSql.getSql();
    log.info("mybatis intercept sql:{}", sql);
    return invocation.proceed();
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}
}
