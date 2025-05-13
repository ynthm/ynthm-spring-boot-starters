package com.ynthm.autoconfigure.mybatis.plus.generate;

import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.INTEGER;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.ynthm.autoconfigure.mybatis.plus.domain.BaseEntity;
import java.util.Collections;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 代码生成器
 *
 * @author Ethan Wang
 */
@Component
public class CodeGenerator {

  private DataSource dataSource;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void generator(DatabaseInfo databaseInfo) {

    FastAutoGenerator.create(new DataSourceConfig.Builder(dataSource)
                    .dbQuery(new MySqlQuery())
                    // tinyint 转 Integer
                    .typeConvertHandler(
                            (globalConfig, typeRegistry, metaInfo) -> {
                              IColumnType columnType = typeRegistry.getColumnType(metaInfo);
                              if (metaInfo.getJdbcType() == JdbcType.TINYINT) {
                                columnType = INTEGER;
                              }
                              return columnType;
                            }
                    ))
            .globalConfig(builder -> {
              builder.author(databaseInfo.getAuthor())
                      .outputDir(databaseInfo.getOutputDir());
            })
            .packageConfig(builder -> {
              builder.parent(databaseInfo.getParentPackage())
                      .moduleName(databaseInfo.getModuleName())
                      // 包名
                      .entity("entity")
                      // 设置 mapper Xml 生成路径
                      .pathInfo(Collections.singletonMap(OutputFile.xml, databaseInfo.getOutputDir() + "/mapper"));
            })
            .strategyConfig(builder -> {
              if (Objects.nonNull(databaseInfo.getTableNames())) {
                builder.addInclude(databaseInfo.getTableNames());
              }
              // 设置过滤表前缀
              builder.addTablePrefix(databaseInfo.getTablePrefix());

              builder.entityBuilder().enableFileOverride()
                      .enableLombok()
                      .idType(IdType.AUTO)
                      .enableChainModel().enableTableFieldAnnotation()
                      .superClass(BaseEntity.class)
                      .addSuperEntityColumns("created_by", "created_time", "last_modified_by", "last_modified_time")
                      .versionPropertyName("version")
                      .logicDeletePropertyName("deleted")
                      .enableActiveRecord()
                      .addTableFills(new Column("create_by", FieldFill.INSERT),
                              new Column("create_time", FieldFill.INSERT));

              builder.controllerBuilder().enableRestStyle()
                      // Controller 路径驼峰改连字符
                      .enableHyphenStyle()
                      .enableFileOverride();

              builder.serviceBuilder().formatServiceFileName("%sService").enableFileOverride();

              builder.mapperBuilder().enableFileOverride();
            })
            // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .templateEngine(new FreemarkerTemplateEngine())
            .execute();
  }
}
