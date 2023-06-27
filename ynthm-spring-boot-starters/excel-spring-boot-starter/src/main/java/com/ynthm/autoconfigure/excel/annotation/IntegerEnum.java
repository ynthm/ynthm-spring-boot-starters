package com.ynthm.autoconfigure.excel.annotation;

/**
 * 返回值禁用枚举 使用 Integer 枚举值
 *
 * <p>推荐导出使用独立的 PO(Struct Mapper 转换)
 *
 * @author Ethan Wang
 * @version 1.0
 */
public interface IntegerEnum extends ExcelEnum {

  Integer value();
}
