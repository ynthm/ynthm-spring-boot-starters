package com.ynthm.spring.web.demo.excel.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表实体字段为 Integer Long 可通过实现 {@link IEnum} 的枚举导出导入转换 {@link EnumConverter}
 *
 * <p>推荐用枚举定义数据库实体中状态类型字段,就不需要这个注解
 *
 * @author Ethan Wang
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEnumFormat {
  /**
   * 要转换的枚举类型
   *
   * @return enum class
   */
  Class<?> value();
}
