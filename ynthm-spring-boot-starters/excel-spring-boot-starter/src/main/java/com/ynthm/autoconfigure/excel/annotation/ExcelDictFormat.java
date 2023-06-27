package com.ynthm.autoconfigure.excel.annotation;

import com.ynthm.autoconfigure.excel.converters.DictIntConverter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库表实体字段为 Integer Long 类型的字典 Excel导出导入转换 {@link DictIntConverter}
 *
 * @author Ethan Wang
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelDictFormat {
  /**
   * 字典 parent code
   *
   * @return 字典 parent code
   */
  String parentCode();
}
