// package com.ynthm.demo.mybatis.ds;
//
// import com.baomidou.mybatisplus.annotation.DbType;
// import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
// import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
// import org.apache.ibatis.plugin.Interceptor;
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.mybatis.spring.SqlSessionTemplate;
// import org.mybatis.spring.annotation.MapperScan;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
// import org.springframework.core.io.support.ResourcePatternResolver;
//
// import javax.sql.DataSource;
//
/// **
// * @author Ynthm Wang
// * @version 1.0
// */
// @Configuration
// @MapperScan(
//    basePackages = {"com.ynthm.demo.mybatis.main.mapper"},
//    sqlSessionFactoryRef = "mainSqlFactory")
// public class MainDataSourceConfig {
//
//  @Bean(name = "mainDataSource")
//  @ConfigurationProperties(prefix = "spring.datasource.main")
//  public DataSource mainDataSource() {
//    return DataSourceBuilder.create().build();
//  }
//
//  @Bean(name = "mainSqlFactory")
//  public SqlSessionFactory mainSqlFactory(
//      @Qualifier("mainDataSource") DataSource dataSource,
//      @Qualifier("secondPaginationInterceptor") MybatisPlusInterceptor paginationInterceptor)
//      throws Exception {
//    MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
//    factoryBean.setDataSource(dataSource);
//    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//    factoryBean.setMapperLocations(resolver.getResources("classpath:main/mapper/**Mapper.xml"));
//    Interceptor[] plugins = new Interceptor[] {paginationInterceptor};
//    factoryBean.setPlugins(plugins);
//    return factoryBean.getObject();
//  }
//
//  @Bean(name = "mainSqlSessionTemplate")
//  public SqlSessionTemplate mainSqlSessionTemplate(
//      @Qualifier("mainSqlFactory") SqlSessionFactory sqlSessionFactory) {
//    return new SqlSessionTemplate(sqlSessionFactory);
//  }
//
//  @Bean("mainPaginationInterceptor")
//  public MybatisPlusInterceptor mysqlInterceptor() {
//    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//    PaginationInnerInterceptor paginationInnerInterceptor =
//        new PaginationInnerInterceptor(DbType.MYSQL);
//    // 设置最大单页限制数量，默认 500 条，-1 不受限制
//    paginationInnerInterceptor.setMaxLimit(500L);
//
//    interceptor.addInnerInterceptor(paginationInnerInterceptor);
//    return interceptor;
//  }
// }
