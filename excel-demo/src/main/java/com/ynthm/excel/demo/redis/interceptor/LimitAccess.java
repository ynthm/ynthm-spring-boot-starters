package com.ynthm.excel.demo.redis.interceptor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/** @author Ethan Wang */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitAccess {

  /**
   * 一定时间窗口的容量
   *
   * @return 窗口容量
   */
  int capacity() default 10;

  /**
   * 时间窗口
   *
   * @return 时间段
   */
  int timeWindow() default 10;

  /**
   * 时间单位
   *
   * @return TimeUnit 默认秒
   */
  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
