package com.ynthm.demo.mybatis.multi;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
  @Override
  protected Object determineCurrentLookupKey() {
    return DataSourceContextHolder.getDataSourceType();
  }
}
