package com.ynthm.common.domain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
public class TimeCost implements AutoCloseable {

  private final long startTime;

  private final String msg;

  public TimeCost(String format, Object... args) {
    this.startTime = System.currentTimeMillis();
    this.msg = String.format(format, args);
  }

  @Override
  public void close() throws Exception {
    log.info("耗时:{}ms, {}", System.currentTimeMillis() - startTime, msg);
  }
}
