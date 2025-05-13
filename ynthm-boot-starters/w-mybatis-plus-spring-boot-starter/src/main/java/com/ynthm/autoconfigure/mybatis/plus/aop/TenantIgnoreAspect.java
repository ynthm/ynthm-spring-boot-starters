package com.ynthm.autoconfigure.mybatis.plus.aop;

import com.ynthm.autoconfigure.mybatis.plus.annotation.IgnoreTenantIsolate;
import org.aopalliance.aop.Advice;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Aspect
@Component
@Order(2)
public class TenantIgnoreAspect {
  @Bean
  public Advisor classAdvisor() {
    AnnotationMatchingPointcut pointcut =
        new AnnotationMatchingPointcut(IgnoreTenantIsolate.class, true);
    Advice advice = new IgnoreTenantAroundAdvice();
    return new DefaultPointcutAdvisor(pointcut, advice);
  }

  @Bean
  public Advisor methodAdvisor() {
    AnnotationMatchingPointcut pointcut =
        AnnotationMatchingPointcut.forMethodAnnotation(IgnoreTenantIsolate.class);
    Advice advice = new IgnoreTenantAroundAdvice();
    return new DefaultPointcutAdvisor(pointcut, advice);
  }
}
