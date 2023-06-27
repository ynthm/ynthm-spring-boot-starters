package com.ynthm.common.web.core.util;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author ethan
 */
public class I18nUtil {

  private final MessageSource messageSource;

  public I18nUtil(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * @param code ：对应messages配置的key.
   * @return message
   */
  public String getMessage(String code) {
    return getMessage(code, null);
  }

  /**
   * @param code ：对应messages配置的key.
   * @param args : 数组参数.
   * @return
   */
  public String getMessage(String code, Object[] args) {
    return getMessage(code, args, "");
  }

  /**
   * @param code ：对应messages配置的key.
   * @param args : 数组参数.
   * @param defaultMessage : 没有设置key的时候的默认值.
   * @return message
   */
  public String getMessage(String code, Object[] args, String defaultMessage) {
    return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
  }

  public String getMessage(String code, Object[] args, Locale locale) {
    return messageSource.getMessage(code, args, locale);
  }
}
