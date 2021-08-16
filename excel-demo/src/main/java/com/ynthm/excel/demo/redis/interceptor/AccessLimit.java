package com.ynthm.excel.demo.redis.interceptor;

import java.lang.annotation.*;

/** @author Ethan Wang */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

  int seconds() default 5;

  int limit() default 5;
}
