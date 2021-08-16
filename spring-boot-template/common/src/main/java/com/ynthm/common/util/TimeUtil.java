package com.ynthm.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class TimeUtil {

  public static final ZoneId GMT8 = ZoneId.of("Asia/Shanghai");
  public static final ZoneId UTC = ZoneId.of("UTC");

  public static final DateTimeFormatter FORMATTER_1 =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public static final DateTimeFormatter FORMATTER_ISO_LDT =
      new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(DateTimeFormatter.ISO_LOCAL_DATE)
          .appendLiteral('T')
          .appendValue(HOUR_OF_DAY, 2)
          .appendLiteral(':')
          .appendValue(MINUTE_OF_HOUR, 2)
          .optionalStart()
          .appendLiteral(':')
          .appendValue(SECOND_OF_MINUTE, 2)
          .appendLiteral('Z')
          .toFormatter();

  /** 2000-01-06T00:00:00Z 2020-01-08T02:39:35.497Z yyyy-MM-dd'T'HH:mm:ss.SSSX */
  public static final DateTimeFormatter FORMATTER_ISO_LDT1 =
      new DateTimeFormatterBuilder()
          .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
          .optionalStart()
          .appendLiteral('.')
          .optionalEnd()
          .optionalStart()
          .appendFraction(ChronoField.MILLI_OF_SECOND, 3, 3, true)
          .optionalEnd()
          .appendLiteral('Z')
          .toFormatter();

  private TimeUtil() {}

  public static LocalDateTime ldtOf(long timestamp, ZoneId at) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    return LocalDateTime.ofInstant(instant, at);
  }

  /**
   * @param timestamp 时间戳 毫秒 13位
   * @return LocalDateTime
   */
  public static LocalDateTime ldtOfUtc(long timestamp) {
    return ldtOf(timestamp, UTC);
  }

  public static OffsetDateTime of(long epochMilli, ZoneId on) {
    Instant ins = Instant.ofEpochMilli(epochMilli);
    return OffsetDateTime.ofInstant(ins, on);
  }

  public static OffsetDateTime ofUtc(long epochMilli) {
    Instant ins = Instant.ofEpochMilli(epochMilli);
    return OffsetDateTime.ofInstant(ins, UTC);
  }

  /**
   * @param localDateTime 原时间
   * @param at 原时间时区
   * @param to 目标时区
   * @return 目标时区的时间
   */
  public static LocalDateTime transfer(LocalDateTime localDateTime, ZoneId at, ZoneId to) {
    ZonedDateTime of = ZonedDateTime.of(localDateTime, at);
    return of.withZoneSameInstant(to).toLocalDateTime();
  }

  /**
   * Date > UTC LocalDateTime
   *
   * @param date Date
   * @return LocalDateTime
   */
  public static LocalDateTime transfer(Date date) {
    return date.toInstant().atZone(UTC).toLocalDateTime();
  }

  /**
   * @param date 原时间
   * @param to 目标时区
   * @return 目标时区的时间
   */
  public static LocalDateTime transfer(Date date, ZoneId to) {
    return date.toInstant().atZone(to).toLocalDateTime();
  }

  public static long parse(String text, ZoneId at) {
    LocalDateTime localDateTime = LocalDateTime.parse(text, FORMATTER_1);
    ZonedDateTime of = ZonedDateTime.of(localDateTime, at);
    return of.toInstant().toEpochMilli();
  }

  public static LocalDateTime getDateTimeOfMilli(long timestamp, ZoneId at) {
    Instant instant = Instant.ofEpochMilli(timestamp);
    return LocalDateTime.ofInstant(instant, at);
  }

  public static long getTimestamp(LocalDateTime localDateTime, ZoneId at) {
    Instant instant = localDateTime.atZone(at).toInstant();
    return instant.toEpochMilli();
  }

  public static LocalDate parseLocalDate(String str) {
    return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static LocalDateTime parseLocalDateTime(String str) {
    return LocalDateTime.parse(str, FORMATTER_1);
  }

  public static long toTimestamp(LocalDateTime localDateTime, ZoneOffset at) {
    return localDateTime.toInstant(at).toEpochMilli();
  }

  public static String format(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  public LocalDateTime toLocalDateTime(long timestamp, ZoneId at) {
    return Instant.ofEpochMilli(timestamp).atZone(at).toLocalDateTime();
  }

  /**
   * 日志、界面展示用
   *
   * @param epochMilli 毫秒
   * @param lo 地区
   * @param zoneId 时区
   * @return 结果字符串
   */
  public static String timestampToString(long epochMilli, Locale lo, ZoneId zoneId) {
    Instant ins = Instant.ofEpochMilli(epochMilli);
    DateTimeFormatter f =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
    return f.withLocale(lo).format(ZonedDateTime.ofInstant(ins, zoneId));
  }
}
