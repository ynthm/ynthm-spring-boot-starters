package com.ynthm.autoconfigure.swagger.config;

import com.ynthm.autoconfigure.swagger.domain.EnumPropertyCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class SwaggerConfig {
  @Bean
  public EnumPropertyCustomizer schemaConfig(@Value("${knife4j.enable:false}") boolean enable) {
    return new EnumPropertyCustomizer(enable);
  }
}
