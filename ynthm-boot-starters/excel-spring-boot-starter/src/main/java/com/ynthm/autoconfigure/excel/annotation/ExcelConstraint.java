package com.ynthm.autoconfigure.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Ethan Wang
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ExcelConstraint {
  /** 定义固定下拉内容 */
  String[] source() default {};
  /** 定义动态下拉内容 */
  Class<? extends ExcelConstraintInterface> sourceMethod() default ExcelConstraintInterface.class;
}
