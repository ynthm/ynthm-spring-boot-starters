package com.ynthm.demo.mybatis.plus;

import com.google.common.collect.Lists;
import com.ynthm.demo.mybatis.plus.generate.CodeGenerator;
import com.ynthm.demo.mybatis.plus.generate.DatabaseInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Ethan Wang
 */
@SpringBootTest
class CodeGeneratorTest {

  @Autowired CodeGenerator codeGenerator;

  @Test
  void generator() {
    DatabaseInfo databaseInfo =
        new DatabaseInfo()
            .setAuthor("Ethan Wang")
            .setOutputDir("D:/generated-code")
            .setModuleName("sync")
            .setParentPackage("com.wb.cross.login")
            .setTablePrefix(Lists.newArrayList("sys_"));

    codeGenerator.generator(databaseInfo);
  }
}
