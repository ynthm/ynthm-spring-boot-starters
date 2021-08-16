package com.ynthm.excel.demo.orm.interceptor;

import com.ynthm.excel.demo.cdc.CdcServiceHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Component
@Intercepts({
  @Signature(
      type = Executor.class,
      method = "update",
      args = {MappedStatement.class, Object.class})
})
public class ExecutorUpdateInterceptor implements Interceptor {
  private Properties properties = new Properties();

  @Autowired private CdcServiceHolder cdcServiceHolder;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object[] args = invocation.getArgs();
    MappedStatement ms = (MappedStatement) args[0];
    Object parameter = args[1];

    Executor executor = (Executor) invocation.getTarget();

    BoundSql boundSql = ms.getBoundSql(parameter);

    SqlCommandType sqlCommandType = ms.getSqlCommandType();
    Object update = null;
    Object before = null;
    Object example = null;
    //    if (SqlCommandType.DELETE.equals(sqlCommandType)) {
    //
    //      String sql = mappedStatement.getBoundSql(param).getSql();
    //
    //    } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
    //
    //      if (param instanceof MapperMethod.ParamMap) {
    //        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) param;
    //        update = paramMap.get("record");
    //        example = paramMap.get("example");
    //      } else {
    //        update = param;
    //        before = cdcServiceHolder.getByClass(update).before(update);
    //      }
    //
    //    } else if (SqlCommandType.INSERT.equals(sqlCommandType)) {
    //      cdcServiceHolder.getByClass(param).insert(param);
    //    }

    // implement pre processing if need
    Object returnObject = invocation.proceed();
    // implement post processing if need
    //    if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
    //      if (param instanceof MapperMethod.ParamMap) {
    //        cdcServiceHolder.getByClass(update).updateByExampleSelective(update, example);
    //      } else {
    //        cdcServiceHolder.getByClass(update).update(update, before);
    //      }
    //    }

    return returnObject;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}
