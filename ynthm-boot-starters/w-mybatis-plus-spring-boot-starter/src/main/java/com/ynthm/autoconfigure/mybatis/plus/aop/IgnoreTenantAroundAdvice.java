package com.ynthm.autoconfigure.mybatis.plus.aop;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.lang.Nullable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class IgnoreTenantAroundAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

  @Override
  public void before(Method method, Object[] args, @Nullable Object target) throws Throwable{
    log.info("before {} called", method.getName());
  }

  @Override
  public void afterReturning(@Nullable Object returnValue, Method method, Object[] args, @Nullable Object target) throws Throwable{
    log.info("after {} called", method.getName());
  }
}
