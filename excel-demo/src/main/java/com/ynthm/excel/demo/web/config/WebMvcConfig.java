package com.ynthm.excel.demo.web.config;

import com.ynthm.excel.demo.redis.interceptor.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实现 WebMvcConfigurer 需要添加 @EnableWebMvc 可以存在多个WebMvcConfigurer
 *
 * <p>不需要返回逻辑视图 WebMvcConfigurationSupport
 *
 * @author Ethan Wang
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired private AccessLimitInterceptor accessLimitInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(accessLimitInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns("/static/**", "/login");
  }
}
