package com.ynthm.demo.mybatis.multi;

import com.ynthm.demo.mybatis.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
public class DataSourceContextHolder {
  private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();

  public static void setDataSourceType(DataSourceType type) {
    if (type == null) {
      throw new NullPointerException();
    }
    log.info("data source change to {}", type);
    contextHolder.set(type);
  }

  public static DataSourceType getDataSourceType() {
    DataSourceType type = contextHolder.get();
    if (type == null) {
      // 确定一个默认数据源
      return DataSourceType.MASTER;
    }
    return type;
  }

  public static void clearDataSourceType() {
    contextHolder.remove();
  }
}
