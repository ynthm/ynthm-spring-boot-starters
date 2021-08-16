package com.ynthm.demo.mybatis.multi;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Aspect
@Component
public class DataSourceAop {

  @Pointcut(
      "@annotation(com.ynthm.demo.mybatis.multi.DataSource)"
          + "|| @within(com.ynthm.demo.mybatis.multi.DataSource)")
  public void dsPointCut() {}

  @Around("dsPointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    Method targetMethod = this.getTargetMethod(point);
    DataSource dataSource = targetMethod.getAnnotation(DataSource.class);
    if (dataSource != null) {
      DataSourceContextHolder.setDataSourceType(dataSource.type());
    }
    try {
      return point.proceed();
    } finally {
      // 销毁数据源 在执行方法之后
      DataSourceContextHolder.clearDataSourceType();
    }
  }

  /** 获取目标方法 */
  private Method getTargetMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
    Signature signature = pjp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method agentMethod = methodSignature.getMethod();
    return pjp.getTarget()
        .getClass()
        .getMethod(agentMethod.getName(), agentMethod.getParameterTypes());
  }
}
