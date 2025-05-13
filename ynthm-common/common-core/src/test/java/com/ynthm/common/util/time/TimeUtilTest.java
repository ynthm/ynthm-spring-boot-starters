package com.ynthm.common.util.time;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class TimeUtilTest {

  @Test
  void convertSecondsToLocalTime() {
    System.out.println(LocalTime.of(8, 30).toSecondOfDay());
    System.out.println(LocalTime.ofSecondOfDay(30600L));
    // 测试示例
    System.out.println(TimeUtil.convertSecondsToLocalTime(0).format(DateTimeFormatter.ISO_LOCAL_TIME)); // 00:00
    System.out.println(TimeUtil.convertSecondsToLocalTime(3661).format(DateTimeFormatter.ISO_LOCAL_TIME)); // 01:01:01
    System.out.println(TimeUtil.convertSecondsToLocalTime(86399).format(DateTimeFormatter.ISO_LOCAL_TIME)); // 23:59:59
    System.out.println(TimeUtil.convertSecondsToLocalTime(-1).format(DateTimeFormatter.ISO_LOCAL_TIME)); // 23:59:59（负数处理）
    System.out.println(TimeUtil.convertSecondsToLocalTime(90000).format(DateTimeFormatter.ISO_LOCAL_TIME)); // 01:00:00（超过一天）
  }

  @Test
  void test01() {
    int a = 830;
    LocalTime localTime =
        LocalTime.parse(String.format("%04d", a), DateTimeFormatter.ofPattern("HHmm"));
    System.out.println(localTime);
    int i = localTime.getHour() * 100 + localTime.getMinute();
    System.out.println(i);
    LocalTime localTime1 = LocalTime.of(i / 100, i % 100);
    System.out.println(localTime1);

    LocalTime time = LocalTime.of(8, 30);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
    String militaryTime = time.format(formatter);
    System.out.println("军用时间: " + militaryTime); // 输出 0830
  }

  @Test
  void toMilitaryTime() {
    System.out.println(TimeUtil.parseMilitaryTime(800));
    System.out.println(TimeUtil.toMilitaryTime(LocalTime.of(8, 0)));
  }
}
