package com.ynthm.common.excel.annotation;

import com.ynthm.common.excel.converter.ExcelEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelConstraint4Enum {
  /**
   * no default has user define default value
   *
   * @return
   */
  Class<? extends Enum<? extends ExcelEnum>> enumFiled();
}
