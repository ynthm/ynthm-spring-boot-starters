package com.ynthm.spring.web.demo.web;

import com.ynthm.common.web.util.UserUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/** @author ethan */
@Component
public class I18nUtil {

  @Resource private MessageSource messageSource;

  /**
   * @param code ：对应messages配置的key.
   * @return
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
   * @return
   */
  public String getMessage(String code, Object[] args, String defaultMessage) {
    Locale locale = UserUtil.getLocale();
    if (locale == null) {
      locale = LocaleContextHolder.getLocale();
    }

    return messageSource.getMessage(code, args, defaultMessage, locale);
  }

  public String getMessage(String code, Object[] args, Locale locale) {
    return messageSource.getMessage(code, args, locale);
  }
}
