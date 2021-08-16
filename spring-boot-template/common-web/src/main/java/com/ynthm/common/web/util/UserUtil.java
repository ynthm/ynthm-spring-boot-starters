package com.ynthm.common.web.util;

import com.ynthm.common.api.IUser;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.exception.BaseException;
import org.slf4j.MDC;

import java.util.Locale;

/** @author ethan */
public class UserUtil {

  private UserUtil() {}

  private static final ThreadLocal<IUser> TL_USER = new ThreadLocal<>();

  private static final ThreadLocal<Locale> TL_LOCALE =
      ThreadLocal.withInitial(
          () ->
              // 语言的默认值
              Locale.SIMPLIFIED_CHINESE);

  public static final String KEY_USER = "user";

  public static void setUser(IUser user) {
    TL_USER.set(user);

    // 把用户信息放到log4j
    MDC.put(KEY_USER, user.getUsername());
  }

  /**
   * 如果没有登录，返回null
   *
   * @return 当前登录用户
   */
  public static IUser getUserIfLogin() {
    return TL_USER.get();
  }

  /**
   * 如果没有登录会抛出异常
   *
   * @return 当前登录用户
   */
  public static IUser getUser() {
    IUser user = TL_USER.get();

    if (user == null) {
      throw new BaseException(ResultCode.USERNAME_NOT_EXIST);
    }

    return user;
  }

  public static void setLocale(Locale locale) {
    TL_LOCALE.set(locale);
  }

  public static Locale getLocale() {
    return TL_LOCALE.get();
  }

  public static void clearAllUserInfo() {
    TL_USER.remove();
    TL_LOCALE.remove();

    MDC.remove(KEY_USER);
  }
}
