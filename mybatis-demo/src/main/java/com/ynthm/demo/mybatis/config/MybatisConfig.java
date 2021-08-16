package com.ynthm.demo.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** @author ethan */
@EnableTransactionManagement
@Configuration
@MapperScan("com.ynthm.demo.mybatis.user.mapper")
public class MybatisConfig {

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    PaginationInnerInterceptor paginationInnerInterceptor =
        new PaginationInnerInterceptor(DbType.MYSQL);
    // 设置最大单页限制数量，默认 500 条，-1 不受限制
    paginationInnerInterceptor.setMaxLimit(500L);

    interceptor.addInnerInterceptor(paginationInnerInterceptor);
    return interceptor;
  }
}
