package com.ynthm.autoconfigure.mybatis.plus.generate;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 代码生成器配置属性
 *
 * @author Ethan Wang
 */
@Data
@Accessors(chain = true)
public class DatabaseInfo {
  private String author = "Ethan Wang";

  private String outputDir;

  private String parentPackage = "com.ynthm.demo";

  private String moduleName;
  private List<String> tableNames;
  private List<String> tablePrefix;
}
