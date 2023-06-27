package com.ynthm.common.web.config;

import com.ynthm.common.web.enums.EnumeratorConverterFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@AutoConfigureBefore({WebMvcAutoConfiguration.class})
@Configuration
public class WebMvcConfig {

  /**
   * 国际化默认解析器
   *
   * @return
   */
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
    return localeResolver;
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumeratorConverterFactory());

        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // 默认 locale
        localeChangeInterceptor.setParamName("lang");
      }
    };
  }
}
