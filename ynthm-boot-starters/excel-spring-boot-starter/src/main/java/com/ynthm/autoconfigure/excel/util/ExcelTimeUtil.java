package com.ynthm.autoconfigure.excel.util;

import com.alibaba.excel.util.StringUtils;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ExcelTimeUtil {

  public static final String DATE_FORMAT_10 = "yyyy-MM-dd";
  public static final String DATE_FORMAT_14 = "yyyyMMddHHmmss";
  public static final String DATE_FORMAT_16 = "yyyy-MM-dd HH:mm";
  public static final String DATE_FORMAT_16_FORWARD_SLASH = "yyyy/MM/dd HH:mm";
  public static final String DATE_FORMAT_17 = "yyyyMMdd HH:mm:ss";
  public static final String DATE_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_19_FORWARD_SLASH = "yyyy/MM/dd HH:mm:ss";
  public static final String DEFAULT_DATE_FORMAT = DATE_FORMAT_19;
  private static final String MINUS = "-";

  private ExcelTimeUtil() {}

  public static OffsetDateTime parseOffsetDateTime(
      String dateString, String dateFormat, Locale local) {
    if (StringUtils.isEmpty(dateFormat)) {
      dateFormat = switchDateFormat(dateString);
    }
    if (local == null) {
      return OffsetDateTime.parse(dateString, DateTimeFormatter.ofPattern(dateFormat));
    } else {
      return OffsetDateTime.parse(dateString, DateTimeFormatter.ofPattern(dateFormat, local));
    }
  }

  public static String format(OffsetDateTime date, String dateFormat, Locale local) {
    if (date == null) {
      return null;
    }
    if (StringUtils.isEmpty(dateFormat)) {
      dateFormat = DEFAULT_DATE_FORMAT;
    }
    if (local == null) {
      return date.format(DateTimeFormatter.ofPattern(dateFormat));
    } else {
      return date.format(DateTimeFormatter.ofPattern(dateFormat, local));
    }
  }

  public static String switchDateFormat(String dateString) {
    int length = dateString.length();
    switch (length) {
      case 19:
        if (dateString.contains(MINUS)) {
          return DATE_FORMAT_19;
        } else {
          return DATE_FORMAT_19_FORWARD_SLASH;
        }
      case 16:
        if (dateString.contains(MINUS)) {
          return DATE_FORMAT_16;
        } else {
          return DATE_FORMAT_16_FORWARD_SLASH;
        }
      case 17:
        return DATE_FORMAT_17;
      case 14:
        return DATE_FORMAT_14;
      case 10:
        return DATE_FORMAT_10;
      default:
        throw new IllegalArgumentException("can not find date format for：" + dateString);
    }
  }
}
