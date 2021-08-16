package com.ynthm.common.util;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.Charset;

/** @author Ethan Wang */
public class StringUtil {
  private StringUtil() {}

  public static boolean isFileSeparator(char c) {
    return '/' == c || '\\' == c;
  }

  /**
   * 编码字符串
   *
   * @param str 字符串
   * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
   * @return 编码后的字节码
   */
  public static byte[] bytes(CharSequence str, Charset charset) {
    if (str == null) {
      return null;
    }

    if (null == charset) {
      return str.toString().getBytes();
    }
    return str.toString().getBytes(charset);
  }

  public static String getName(String filePath) {
    if (null == filePath) {
      return null;
    } else {
      int len = filePath.length();
      if (0 == len) {
        return filePath;
      } else {
        if (isFileSeparator(filePath.charAt(len - 1))) {
          --len;
        }

        int begin = 0;

        for (int i = len - 1; i > -1; --i) {
          char c = filePath.charAt(i);
          if (isFileSeparator(c)) {
            begin = i + 1;
            break;
          }
        }

        return filePath.substring(begin, len);
      }
    }
  }

  public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
    return endWith(str, suffix, true);
  }

  public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
    if (null != str && null != suffix) {
      return isIgnoreCase
          ? str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase())
          : str.toString().endsWith(suffix.toString());
    } else {
      return null == str && null == suffix;
    }
  }

  public static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }

  public static <T> boolean isArrayEmpty(T[] array) {
    return array == null || array.length == 0;
  }

  public static boolean endWithAny(
      boolean isIgnoreCase, CharSequence str, CharSequence... suffixes) {
    if (!isEmpty(str) && !isArrayEmpty(suffixes)) {

      for (CharSequence suffix : suffixes) {
        if (endWith(str, suffix, false)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
    return startWith(str, prefix, true);
  }

  public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
    return startWith(str, prefix, ignoreCase, false);
  }

  public static boolean startWith(
      CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
    if (null != str && null != prefix) {
      boolean isStartWith;
      if (ignoreCase) {
        isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
      } else {
        isStartWith = str.toString().startsWith(prefix.toString());
      }

      if (!isStartWith) {
        return false;
      } else {
        return !ignoreEquals || !equals(str, prefix, ignoreCase);
      }
    } else if (!ignoreEquals) {
      return false;
    } else {
      return null == str && null == prefix;
    }
  }

  public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
    if (null == str1) {
      return str2 == null;
    } else if (null == str2) {
      return false;
    } else {
      return ignoreCase
          ? str1.toString().equalsIgnoreCase(str2.toString())
          : str1.toString().contentEquals(str2);
    }
  }

  public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
    if (!isEmpty(str) && !isArrayEmpty(suffixes)) {

      for (CharSequence suffix : suffixes) {
        if (endWith(str, suffix, true)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isNotBlank(CharSequence cs) {
    return !isBlank(cs);
  }

  public static boolean isBlank(CharSequence str) {
    int length;
    if (str != null && (length = str.length()) != 0) {
      for (int i = 0; i < length; ++i) {
        if (!CharUtil.isBlankChar(str.charAt(i))) {
          return false;
        }
      }

      return true;
    } else {
      return true;
    }
  }

  public static String[] split(String str, char separatorChar) {
    return StrUtil.splitToArray(str, separatorChar);
  }
}
