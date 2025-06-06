package com.ynthm.autoconfigure.mybatis.plus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ynthm.autoconfigure.mybatis.plus.util.TableInfoUtil;
import com.ynthm.common.context.AuthUser;
import com.ynthm.common.context.UserContext;
import com.ynthm.common.context.UserContextHolder;
import com.ynthm.common.web.core.IUser;
import java.util.Optional;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Mybatis Plus 配置类
 *
 * @author Ethan Wang
 */
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisPlusConfig {

  @ConditionalOnProperty(
      name = {"ynthm.mybatis.mapper-scan.enabled"},
      havingValue = "true")
  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer(MybatisProperties mybatisProperties) {
    MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
    // 可以通过环境变量获取你的 mapper 路径，这样 mapper 扫描可以通过配置文件配置了
    scannerConfigurer.setBasePackage(mybatisProperties.getBasePackage());
    return scannerConfigurer;
  }

  @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // 分页插件
    PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
    pageInterceptor.setMaxLimit(10000L);
    interceptor.addInnerInterceptor(pageInterceptor);
    // 乐观锁插件 仅支持 updateById(id) 与 update(entity, wrapper) 方法
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    // 针对 update 和 delete 语句 作用: 阻止恶意的全表更新删除
    interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

    interceptor.addInnerInterceptor(
        new TenantLineInnerInterceptor(
            new TenantLineHandler() {
              @Override
              public Expression getTenantId() {

                return Optional.ofNullable(UserContextHolder.getContext())
                    .map(UserContext::getAuthUser)
                    .map(AuthUser::getPrincipal)
                    .map(i -> (IUser) i)
                    .map(IUser::tenantId)
                    .map(LongValue::new)
                    .orElse(null);
              }

              @Override
              public boolean ignoreTable(String tableName) {
                return TableInfoUtil.noneTenantIdTables().contains(tableName);
              }
            }));
    return interceptor;
  }

  @Bean
  public ConfigurationCustomizer configurationCustomizer() {
    return configuration -> configuration.setLogImpl(Slf4jImpl.class);
  }

  @ConditionalOnMissingBean(MetaObjectHandler.class)
  @Bean
  public EntityMetaObjectHandler entityMetaObjectHandler() {
    return new EntityMetaObjectHandler();
  }
}
