package com.ynthm.excel.demo.annotation;

import java.lang.annotation.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Map2Field {}
