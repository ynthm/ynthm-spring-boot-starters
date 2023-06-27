package com.ynthm.demo.mybatis.plus.user.mapper;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;

/**
 * @author Ethan Wang
 */
@Rollback(value = false)
@ImportAutoConfiguration({MybatisPlusConfig.class})
@MybatisPlusTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseMapperTest {
}
