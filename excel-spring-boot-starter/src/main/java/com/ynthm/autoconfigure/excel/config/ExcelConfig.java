package com.ynthm.autoconfigure.excel.config;

import com.ynthm.autoconfigure.excel.util.ExcelHelper;
import org.springframework.context.annotation.Bean;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ExcelConfig {
  @Bean
  public ExcelHelper excelHelper(){
   return new ExcelHelper();
  }
}
