//package com.ynthm.autoconfigure.mybatis.plus.aop;
//
//import com.ynthm.autoconfigure.mybatis.plus.annotation.IgnoreTenantIsolate;
//import java.lang.reflect.Method;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
///**
// * @author Ethan Wang
// * @version 1.0
// */
//@Aspect
//@Component
//@Order(2)
//public class TenantIgnoreAspectV2 {
//  @Pointcut(
//      "@within(com.ynthm.autoconfigure.mybatis.plus.annotation.IgnoreTenantIsolate)||@annotation(com.ynthm.autoconfigure.mybatis.plus.annotation.IgnoreTenantIsolate)")
//  public void pointcut() {}
//
//  @Around("pointcut()")
//  public Object around(ProceedingJoinPoint point) throws Throwable {
//    try {
//      MethodSignature signature = (MethodSignature) point.getSignature();
//      Method method = signature.getMethod();
//      if (method.getDeclaringClass().isAnnotationPresent(IgnoreTenantIsolate.class)) {
//        TenantIsolationContext.set(true);
//      } else {
//        if (method.isAnnotationPresent(IgnoreTenantIsolate.class)) {
//          TenantIsolationContext.set(true);
//        }
//      }
//      return point.proceed();
//    } finally {
//      TenantIsolationContext.clear();
//    }
//  }
//}
