package com.ynthm.excel.demo.excel.excel;

import java.lang.annotation.*;

/** @author Ethan Wang */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExplicitConstraint {
  /** 定义固定下拉内容 */
  String[] source() default {};
  /** 定义动态下拉内容 */
  Class[] sourceClass() default {};
}
