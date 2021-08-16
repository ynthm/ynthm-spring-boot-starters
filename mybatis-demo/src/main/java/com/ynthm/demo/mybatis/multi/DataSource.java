package com.ynthm.demo.mybatis.multi;

import com.ynthm.demo.mybatis.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
  DataSourceType type() default DataSourceType.MASTER;
}
