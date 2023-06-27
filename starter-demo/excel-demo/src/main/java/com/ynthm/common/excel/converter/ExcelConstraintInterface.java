package com.ynthm.common.excel.converter;

import java.util.Map;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface ExcelConstraintInterface {
  /**
   * 获取拉列表的内容数组
   *
   * @param params 可以为空
   * @return 下拉框可选数据
   */
  String[] source(Map<String, Object> params);
}
