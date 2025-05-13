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
    long seconds = (System.currentTimeMillis() - startTime) / 1000; // 毫秒转换为秒
    long minutes = seconds / 60L; // 秒转换为分钟
    long hours = minutes / 60L; // 分钟转换为小时
    seconds = seconds % 60L; // 剩余的秒数
    minutes = minutes % 60L; // 剩余的分钟数
    String cost =
        (hours > 0 ? hours + "小时" : "") + (minutes > 0 ? minutes + "分钟" : "") + seconds + "秒";
    log.info("耗时:{} --- {}", cost, msg);
  }
}
