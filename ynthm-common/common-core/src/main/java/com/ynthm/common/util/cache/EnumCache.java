package com.ynthm.common.util.cache;

import com.ynthm.common.util.CastUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumCache {
  private static final Map<Class<?>, Map<Object, Enum<?>>> ENUM_CACHE =
      new ConcurrentHashMap<>(128);

  private EnumCache() {}

  /**
   * 通过值获取枚举，结果可能为 null
   *
   * @param enumClass 枚举类型 结果类型
   * @param enum2Value 枚举到缓存值
   * @param value 缓存值
   * @return 枚举值
   */
  public static <E extends Enum<E>> E getEnum(
      Class<E> enumClass, Function<E, Object> enum2Value, Object value) {
    if (ENUM_CACHE.containsKey(enumClass)) {
      return CastUtil.cast(ENUM_CACHE.get(enumClass).get(value));
    }

    E[] enumConstants = enumClass.getEnumConstants();
    Map<Object, Enum<?>> value2Enum = new HashMap<>(enumConstants.length);
    for (E item : enumConstants) {
      value2Enum.put(enum2Value.apply(item), item);
    }
    ENUM_CACHE.put(enumClass, value2Enum);

    return CastUtil.cast(ENUM_CACHE.get(enumClass).get(value));
  }
}
