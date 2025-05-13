package com.ynthm.common.util.time;

import static java.time.temporal.ChronoField.*;

import com.ynthm.common.util.PeriodDay;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class TimeUtil {

  public static final ZoneId GMT8 = ZoneId.of("Asia/Shanghai");
  public static final ZoneId UTC = ZoneId.of("UTC");

  public static final DateTimeFormatter DTF_LDT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  /**
   * 匹配 4位年份 1~2位月份 1~2位天
   *
   * <p>等价 DateTimeFormatterBuilder.appendValue(YEAR, 4, 10,
   * SignStyle.EXCEEDS_PAD).appendLiteral('/').appendValue(MONTH_OF_YEAR).appendValue(DAY_OF_MONTH).toFormatter();
   */
  public static final DateTimeFormatter DTF_DF_1 = DateTimeFormatter.ofPattern("yyyy/M/d");

  public static final DateTimeFormatter DTF_DF_2;
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

  static {
    DTF_DF_2 =
        new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NORMAL)
            .toFormatter();
  }

  private TimeUtil() {}

  public static LocalDateTime ldtOf(Instant instant, ZoneId at) {
    return LocalDateTime.ofInstant(instant, at);
  }

  /**
   * {@link Instant}转{@link LocalDateTime}，使用默认时区
   *
   * @param instant {@link Instant}
   * @return {@link LocalDateTime}
   */
  public static LocalDateTime ldtOf(Instant instant) {
    return ldtOf(instant, ZoneId.systemDefault());
  }

  /**
   * {@link Instant}转{@link LocalDateTime}，使用UTC时区
   *
   * @param instant {@link Instant}
   * @return {@link LocalDateTime}
   */
  public static LocalDateTime ldtOfUtc(Instant instant) {
    return ldtOf(instant, UTC);
  }

  public static LocalDateTime ldtOf(long epochMilli, ZoneId at) {
    return ldtOf(Instant.ofEpochMilli(epochMilli), at);
  }

  /**
   * @param timestamp 时间戳 毫秒 13位
   * @return LocalDateTime
   */
  public static LocalDateTime ldtOfUtc(long timestamp) {
    return ldtOf(timestamp, UTC);
  }

  public static LocalDateTime ldtOf(long epochMilli) {
    return ldtOf(epochMilli, ZoneId.systemDefault());
  }

  public static ZoneId zoneId(TimeZone timeZone) {
    return timeZone.toZoneId();
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

  public static long getTimestamp(LocalDateTime localDateTime, ZoneId at) {
    Instant instant = localDateTime.atZone(at).toInstant();
    return instant.toEpochMilli();
  }

  public static LocalDate parseLocalDate(String str) {
    return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static LocalDateTime parseLocalDateTime(String str) {
    return LocalDateTime.parse(str, DTF_LDT);
  }

  public static long toTimestamp(LocalDateTime localDateTime, ZoneOffset at) {
    return localDateTime.toInstant(at).toEpochMilli();
  }

  public static Instant instantOf(LocalDateTime localDateTime, ZoneId at) {
    return localDateTime.atZone(at).toInstant();
  }

  public static Instant instantOf(long epochMilli) {
    return Instant.ofEpochMilli(epochMilli);
  }

  /** 取本月第一天 */
  public static LocalDate firstDayOfThisMonth() {
    LocalDate today = LocalDate.now();
    return today.with(TemporalAdjusters.firstDayOfMonth());
  }

  /** 取本月最后一天 */
  public static LocalDate lastDayOfThisMonth() {
    LocalDate today = LocalDate.now();
    return today.with(TemporalAdjusters.lastDayOfMonth());
  }

  /** 取本月第一天的开始时间 */
  public static LocalDateTime startOfThisMonth() {
    return LocalDateTime.of(firstDayOfThisMonth(), LocalTime.MIN);
  }

  /** 取本月最后一天的结束时间 */
  public static LocalDateTime endOfThisMonth() {
    return LocalDateTime.of(lastDayOfThisMonth(), LocalTime.MAX);
  }

  public static String format(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
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

  public static long parse(String text, ZoneId at) {
    return parseInstant(text, DTF_LDT, at).toEpochMilli();
  }

  public static Instant parseInstant(String text, DateTimeFormatter formatter, ZoneId at) {
    LocalDateTime localDateTime = LocalDateTime.parse(text, formatter);
    ZonedDateTime of = ZonedDateTime.of(localDateTime, at);
    return of.toInstant();
  }

  public static PeriodDay periodDay(Duration duration) {
    long days = duration.toDays();
    Duration durationMinusDays = duration.minusDays(days);
    long hours = durationMinusDays.toHours();
    Duration durationMinusDaysAndHours = durationMinusDays.minusHours(hours);
    long minutes = durationMinusDaysAndHours.toMinutes();

    return new PeriodDay()
        .setDays(days)
        .setHours(hours)
        .setMinutes(minutes)
        .setSeconds(durationMinusDaysAndHours.minusMinutes(minutes).toMillis() / 1000);
  }

  public static String formatTimeCost(long milliseconds) {
    long seconds = milliseconds / 1000; // 毫秒转换为秒
    long minutes = seconds / 60L; // 秒转换为分钟
    long hours = minutes / 60L; // 分钟转换为小时
    seconds = seconds % 60L; // 剩余的秒数
    minutes = minutes % 60L; // 剩余的分钟数
    return (hours > 0 ? hours + "小时" : "") + (minutes > 0 ? minutes + "分钟" : "") + seconds + "秒";
  }

  /**
   * 推荐直接使用 LocalTime 提供方法
   *
   * @see LocalTime#toSecondOfDay()
   * @see LocalTime#ofSecondOfDay(long)
   */
  public static LocalTime convertSecondsToLocalTime(long totalSeconds) {
    // 调整秒数到 0-86399 之间
    long adjustedSeconds = (totalSeconds % 86400 + 86400) % 86400;

    int hours = (int) (adjustedSeconds / 3600);
    int remainingSeconds = (int) (adjustedSeconds % 3600);
    int minutes = remainingSeconds / 60;
    int seconds = remainingSeconds % 60;

    return LocalTime.of(hours, minutes, seconds);
  }

  public static LocalTime parseMilitaryTime(int input) {
    int hours = input / 100;
    int minutes = input % 100;
    if (hours > 23) {
      throw new IllegalArgumentException("无效的军用时间格式: " + input);
    }
    return LocalTime.of(hours, minutes);
  }

  public static int toMilitaryTime(LocalTime input) {
    return input.getHour() * 100 + input.getMinute();
  }

  public LocalDateTime toLocalDateTime(long timestamp, ZoneId at) {
    return Instant.ofEpochMilli(timestamp).atZone(at).toLocalDateTime();
  }
}
