package com.ynthm.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
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
  void format() {}

  @Test
  void toLocalDateTime() {}

  @Test
  void timestampToString() {}

  @Test
  void test1() {
    Long millisecond = Instant.now().toEpochMilli(); // 精确到毫秒
    Long second = Instant.now().getEpochSecond(); // 精确到秒
    System.out.println(millisecond);
    System.out.println(System.currentTimeMillis());

    System.out.println(OffsetDateTime.now().getOffset());
  }

  @Test
  void testZone() {
    // 获取所有可用的时区
    Set<String> allZones = ZoneId.getAvailableZoneIds();

    // 按自然顺序排序
    // Create a List using the set of zones and sort it.
    List<String> zoneList = new ArrayList<>(allZones);
    Collections.sort(zoneList);

    LocalDateTime dt = LocalDateTime.now();
    for (String s : zoneList) {
      // 获取到的字符串可以通过ZoneId.of获取实例
      ZoneId zone = ZoneId.of(s);
      // 把本地时间加时区信息 转换成一个ZonedDateTime
      // 但是这个LocalDateTime不包含时区信息，是怎么计算出来的呢？本地时间与这个时区相差n小时？
      // 这里的偏移量是针对 格林威治标准时间来说的 +3 ，就是比标准时间快3个小时
      // 如果说一个时区是 +3;而北京是+8，那么该时区比北京慢5个小时
      // 北京时间是12点，那么该时区12-5 = 7
      ZonedDateTime zdt = dt.atZone(zone);
      ZoneOffset offset = zdt.getOffset();
      int secondsOfHour = offset.getTotalSeconds() % (60 * 60);
      String out = String.format("%35s %10s%n", zone, offset);

      // Write only time zones that do not have a whole hour offset
      // to standard out.
      //  if (secondsOfHour != 0) {
      System.out.printf(out);
      // }
    }
  }

  @Test
  void test01() {
    Map<String, String> sortedMap = new LinkedHashMap<>();

    List<String> zoneList = new ArrayList<>(ZoneId.getAvailableZoneIds());

    // Get all ZoneIds
    Map<String, String> allZoneIds = getAllZoneIds(zoneList);

    // sort map by key
    /*allZoneIds.entrySet().stream()
    .sorted(Map.Entry.comparingByKey())
    .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));*/

    // sort by value, descending order
    allZoneIds.entrySet().stream()
        .sorted(Map.Entry.<String, String>comparingByValue().reversed())
        .forEachOrdered(e -> sortedMap.put(e.getKey(), e.getValue()));

    // print map
    sortedMap.forEach(
        (k, v) -> {
          String out = String.format("%35s (UTC%s) %n", k, v);
          System.out.printf(out);
        });

    System.out.println("\nTotal Zone IDs " + sortedMap.size());
  }

  private Map<String, String> getAllZoneIds(List<String> zoneList) {

    Map<String, String> result = new HashMap<>();

    LocalDateTime dt = LocalDateTime.now();

    for (String zoneId : zoneList) {

      ZoneId zone = ZoneId.of(zoneId);
      ZonedDateTime zdt = dt.atZone(zone);
      ZoneOffset zos = zdt.getOffset();

      // replace Z to +00:00
      String offset = zos.getId().replaceAll("Z", "+00:00");

      result.put(zone.toString(), offset);
    }

    return result;
  }

  @Test
  void testOfUtc() {
    System.out.println(TimeUtil.periodDay(Duration.ofSeconds(70)));
    LocalDateTime localDateTime1 = LocalDateTime.of(2019, 11, 15, 0, 0);
    LocalDateTime localDateTime2 = LocalDateTime.of(2020, 12, 16, 10, 30);

    System.out.println(localDateTime1.until(localDateTime2, ChronoUnit.HOURS));

    Duration d = Duration.between(localDateTime1, localDateTime2);
    System.out.println(d.get(ChronoUnit.SECONDS));
    System.out.println("days:" + d.toDays());
    System.out.println("hours:" + d.toHours());
    System.out.println("minutes:" + d.toMinutes());
    System.out.println("millis:" + d.toMillis());

    long hours = d.minusDays(d.toDays()).toHours();
    System.out.println(hours);
    System.out.println(d.minusDays(d.toDays()).minusHours(hours).toMinutes());

    Period period = Period.between(localDateTime1.toLocalDate(), localDateTime2.toLocalDate());
    Duration duration =
        Duration.between(localDateTime1.toLocalTime(), localDateTime2.toLocalTime());
    System.out.println(period.getYears() + "年");
    System.out.println(period.getMonths() + "月");
    System.out.println(period.getDays() + "日");

    System.out.println(duration.toHours());
    System.out.println(duration.toMinutes());
    System.out.println(duration.get(ChronoUnit.SECONDS));
    System.out.println(duration.toNanos());
  }
}
