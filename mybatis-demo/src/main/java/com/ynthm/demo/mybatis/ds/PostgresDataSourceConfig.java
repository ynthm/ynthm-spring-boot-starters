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
// * 只适合多个数据源的结构完全不一样，通过package可以分来的方式来调用，不能灵活的在一个package下面随心所欲的调用数据源。 如果用myBatis, SqlSessionFactory
// * 部分可以使用SqlSessionFactoryBean来生成。但是如果用mybatis plus一定要用MybatisSqlSessionFactoryBean
// * 来生成SqlSessionFactory。
// *
// * @author Ynthm Wang
// * @version 1.0
// */
// @Configuration
// @MapperScan(
//    basePackages = {"com.ynthm.demo.mybatis.second.mapper"},
//    sqlSessionTemplateRef = "secondSqlSessionTemplate")
// public class PostgresDataSourceConfig {
//
//  @Bean(name = "secondDataSource")
//  @ConfigurationProperties(prefix = "spring.datasource.second")
//  public DataSource druidDataSource1() {
//    return DataSourceBuilder.create().build();
//  }
//
//  @Bean(name = "secondSqlSessionFactory")
//  public SqlSessionFactory sqlSessionFactory(
//      @Qualifier("secondDataSource") DataSource dataSource,
//      @Qualifier("secondPaginationInterceptor") MybatisPlusInterceptor paginationInterceptor)
//      throws Exception {
//    MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
//    factoryBean.setDataSource(dataSource);
//    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//    factoryBean.setMapperLocations(resolver.getResources("classpath:second/mapper/**Mapper.xml"));
//    Interceptor[] plugins = new Interceptor[] {paginationInterceptor};
//    factoryBean.setPlugins(plugins);
//    return factoryBean.getObject();
//  }
//
//  @Bean(name = "secondSqlSessionTemplate")
//  public SqlSessionTemplate sqlSessionTemplate(
//      @Qualifier("secondSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//    return new SqlSessionTemplate(sqlSessionFactory);
//  }
//
//  @Bean("secondPaginationInterceptor")
//  public MybatisPlusInterceptor postgresInterceptor() {
//    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//    PaginationInnerInterceptor paginationInnerInterceptor =
//        new PaginationInnerInterceptor(DbType.POSTGRE_SQL);
//    // 设置最大单页限制数量，默认 500 条，-1 不受限制
//    paginationInnerInterceptor.setMaxLimit(500L);
//
//    interceptor.addInnerInterceptor(paginationInnerInterceptor);
//    return interceptor;
//  }
// }
