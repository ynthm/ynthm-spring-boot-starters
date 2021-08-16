package com.ynthm.demo.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/** @author ynthm wang */
@Slf4j
@SpringBootApplication
public class MybatisDemoApplication implements ApplicationRunner {

  public static void main(String[] args) {
    // 数据库时间有时区，数据库是UTC时间会自动转化
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(MybatisDemoApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("##########  Application Running  ##########");
    log.info(
        "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 "
            + "\uD83E\uDD23 \uD83D\uDE02 \uD83D\uDE05 \uD83D\uDE06 \uD83D\uDE01 \uD83D\uDE04 \uD83D\uDE03 \uD83D\uDE00");
  }
}
