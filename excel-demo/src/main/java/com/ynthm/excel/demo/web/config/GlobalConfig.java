package com.ynthm.excel.demo.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** @author Ethan Wang */
@Component
@ConfigurationProperties(prefix = "global")
public class GlobalConfig {
  /** 上传路径 */
  private static String downloadPath;

  public static String getDownloadPath() {
    return downloadPath;
  }
}
