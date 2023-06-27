package com.ynthm.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class CastUtil {

  private CastUtil() {}

  @SuppressWarnings("unchecked")
  public static <T> T cast(Object obj) {
    return (T) obj;
  }

  public static <T> List<T> castList(Object obj, Class<T> clazz)
  {
    List<T> result = new ArrayList<>();
    if(obj instanceof List<?>)
    {
      for (Object o : (List<?>) obj)
      {
        result.add(clazz.cast(o));
      }
      return result;
    }
    return null;
  }
}
