package com.ynthm.common.web.core.config;

import com.ynthm.common.web.core.util.I18nUtil;
import com.ynthm.common.web.core.util.SpringBeanUtil;
import com.ynthm.common.web.core.util.SpringContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Configuration
public class WebCoreConfig {
  @Bean
  public SpringBeanUtil springBeanUtil() {
    return new SpringBeanUtil();
  }

  @Bean
  public SpringContextHolder springContextHolder() {
    return new SpringContextHolder();
  }

  @Bean
  public I18nUtil i18nUtil(MessageSource messageSource) {
    return new I18nUtil(messageSource);
  }
}
