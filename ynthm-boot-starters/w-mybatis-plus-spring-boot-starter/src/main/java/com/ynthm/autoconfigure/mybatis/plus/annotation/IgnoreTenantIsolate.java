package com.ynthm.autoconfigure.mybatis.plus.annotation;

import java.lang.annotation.*;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface IgnoreTenantIsolate {

  /** ture 不做租户隔离 */
  boolean ignored() default true;
}
