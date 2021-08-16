package com.ynthm.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

class TimeUtilTest {

  @Test
  void ldtOf() {}

  @Test
  void ldtOfUtc() {}

  @Test
  void of() {}

  @Test
  void ofUtc() {}

  @Test
  void transfer() {
    Date now = new Date();
    LocalDateTime transfer = TimeUtil.transfer(now);
    System.out.println(TimeUtil.format(transfer));
    LocalDateTime transfer1 = TimeUtil.transfer(now, TimeUtil.GMT8);
    System.out.println(TimeUtil.format(transfer1));
    Assertions.assertTrue(transfer.isBefore(transfer1));
  }

  @Test
  void testTransfer() {}

  @Test
  void testTransfer1() {}

  @Test
  void parse() {}

  @Test
  void getDateTimeOfMilli() {}

  @Test
  void getTimestamp() {}

  @Test
  void parseLocalDate() {}

  @Test
  void parseLocalDateTime() {}

  @Test
  void toTimestamp() {}

  @Test
  void toLocalDateTime() {}

  @Test
  void timestampToString() {}
}
