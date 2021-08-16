// package com.ynthm.demo.mybatis.multi;
//
// import com.ynthm.demo.mybatis.enums.DataSourceType;
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.mybatis.spring.SqlSessionFactoryBean;
// import org.mybatis.spring.annotation.MapperScan;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.env.Environment;
// import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
// import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
// import javax.sql.DataSource;
// import java.util.HashMap;
// import java.util.Map;
//
/// **
// * @author Ynthm Wang
// * @version 1.0
// */
// @Configuration
// @MapperScan("com.ynthm.demo.mybatis.multi.mapper")
// public class DataSourceConfig {
//
//  @Autowired private Environment env;
//
//  @Bean(name = "datasource1")
//  @ConfigurationProperties(prefix = "spring.datasource.master")
//  public DataSource druidDataSource1() {
//    return DataSourceBuilder.create().build();
//  }
//
//  @Bean(name = "datasource2")
//  @ConfigurationProperties(prefix = "spring.datasource.slave")
//  public DataSource druidDataSource2() {
//    return DataSourceBuilder.create().build();
//  }
//
//  @Bean
//  public DynamicDataSource dynamicDataSource(
//      @Qualifier("datasource1") DataSource ds1, @Qualifier("datasource2") DataSource ds2) {
//    Map<Object, Object> targetDataSource = new HashMap<>(2);
//    targetDataSource.put(DataSourceType.MASTER, ds1);
//    targetDataSource.put(DataSourceType.SLAVE, ds2);
//    DynamicDataSource dataSource = new DynamicDataSource();
//    dataSource.setTargetDataSources(targetDataSource);
//    dataSource.setDefaultTargetDataSource(ds1);
//    dataSource.afterPropertiesSet();
//    return dataSource;
//  }
//
//  @Bean
//  public SqlSessionFactory dynamicSqlSessionFactory(DynamicDataSource dynamicDataSource)
//      throws Exception {
//
//    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//    // 指定数据源
//    bean.setDataSource(dynamicDataSource);
//    bean.setMapperLocations(
//        new PathMatchingResourcePatternResolver().getResource("classpath*:mapper/**Mapper.xml"));
//
//    return bean.getObject();
//  }
//
//  @Bean
//  public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
//    return new DataSourceTransactionManager(dynamicDataSource);
//  }
// }
