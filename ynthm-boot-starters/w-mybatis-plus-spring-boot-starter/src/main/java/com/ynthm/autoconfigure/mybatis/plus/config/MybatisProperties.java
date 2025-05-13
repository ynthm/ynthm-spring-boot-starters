package com.ynthm.autoconfigure.mybatis.plus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@ConfigurationProperties(prefix = "ynthm.mybatis.mapper-scan")
public class MybatisProperties {
  private Boolean enabled;

  private String basePackage = "com.ynthm.**.mapper";
}
