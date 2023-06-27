package com.ynthm.common.excel.annotation;

import java.lang.annotation.*;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelEnumClass {
  Class<? extends Enum<?>> value();
}
