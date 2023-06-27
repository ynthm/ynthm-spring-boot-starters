package com.ynthm.autoconfigure.mybatis.plus.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@ConditionalOnProperty(
        name = {"ynthm.mybatis.mapper-scan.enabled"},
        havingValue = "true"
)
@MapperScan(value = "${ynthm.mybatis.mapper-scan.base-package:com.ynthm.**.mapper}")
public class BaseMapperScanConfig {
}
