package com.ynthm.autoconfigure.mybatis.plus.util;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ynthm.autoconfigure.mybatis.plus.config.SqlConstant;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class TableInfoUtil {
  private static final Set<String> noneTenantIdTables = new ConcurrentSkipListSet<>();

  private TableInfoUtil() {}

  public static Set<String> noneTenantIdTables() {
    if (noneTenantIdTables.isEmpty()) {
      for (TableInfo tableInfo : TableInfoHelper.getTableInfos()) {
        boolean hasTenantId = false;
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
          if (SqlConstant.TENANT_ID_COLUMN.equals(tableFieldInfo.getColumn())) {
            hasTenantId = true;
            break;
          }
        }
        if (!hasTenantId) {
          noneTenantIdTables.add(tableInfo.getTableName());
        }
      }
    }

    return noneTenantIdTables;
  }
}
