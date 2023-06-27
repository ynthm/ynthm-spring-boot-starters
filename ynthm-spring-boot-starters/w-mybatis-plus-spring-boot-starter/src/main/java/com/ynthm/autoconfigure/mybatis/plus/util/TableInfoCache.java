package com.ynthm.autoconfigure.mybatis.plus.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class TableInfoCache {
  private TableInfoCache() {}

  public static final Cache<String, Set<String>> TABLE_FIELDS = Caffeine.newBuilder().build();

  /**
   * 启动时 information_schema.`TABLES` information_schema.`COLUMNS` 查询出没有 tenant_id 的表 微服务结合 redis 缓存
   */
  public static final Set<String> IGNORE_TABLES = new CopyOnWriteArraySet<>();
}
