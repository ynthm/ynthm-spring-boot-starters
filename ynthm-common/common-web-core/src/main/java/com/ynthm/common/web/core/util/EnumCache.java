package com.ynthm.common.web.core.util;

import com.ynthm.common.enums.EnumValue;
import com.ynthm.common.util.CastUtil;
import com.ynthm.common.web.core.enums.Enumerator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumCache {
  private static final Map<Class<?>, Map<Object, Enum<?>>> ENUM_CACHE =
      new ConcurrentHashMap<>(128);

  private EnumCache() {}

  /** for Enumerator */
  public static <V extends Serializable, E extends Enum<E>> Optional<E> getEnum(
      Class<E> enumClass, V value) {
    if (ENUM_CACHE.containsKey(enumClass)) {
      return Optional.ofNullable(CastUtil.cast(ENUM_CACHE.get(enumClass).get(value)));
    }
    Map<Object, Enum<?>> value2Enum = new HashMap<>();

    for (E item : enumClass.getEnumConstants()) {
      if (item instanceof Enumerator) {
        value2Enum.put(((Enumerator<?>) item).value(), item);
      }
    }

    ENUM_CACHE.put(enumClass, value2Enum);
    return Optional.ofNullable(CastUtil.cast(value2Enum.get(value)));
  }

  /**
   * 通过通用 Class 获取枚举
   *
   * @param enumClass 枚举类型
   * @param value 枚举真实值
   * @return 可选 枚举
   */
  public static <V extends Serializable, E extends Enum<E>> Optional<E> getEnumByWildcardClass(
      Class<? extends Enumerator<?>> enumClass, V value) {
    if (ENUM_CACHE.containsKey(enumClass)) {
      return Optional.ofNullable(CastUtil.cast(ENUM_CACHE.get(enumClass).get(value)));
    }

    Enumerator<?>[] enumConstants = enumClass.getEnumConstants();
    Map<Object, Enum<?>> value2Enum = new HashMap<>(enumConstants.length);

    for (Enumerator<?> item : enumConstants) {
      value2Enum.put(item.value(), (Enum<?>) item);
    }

    ENUM_CACHE.put(enumClass, value2Enum);
    return Optional.ofNullable(CastUtil.cast(value2Enum.get(value)));
  }

  /**
   * 枚举真实值类型
   *
   * <p>存入数据库得知; 业务传递的值
   */
  public static boolean valueInt(Class<?> enumClass) {
    if (Enumerator.class.isAssignableFrom(enumClass)) {
      return Arrays.stream(enumClass.getEnumConstants())
          .findFirst()
          .map(i -> (Enumerator<?>) i)
          .map(EnumValue::value)
          .map(Integer.class::isInstance)
          .orElse(false);
    }
    return false;
  }

  public static <V extends Serializable, E extends Enumerator<?>> Optional<E> getEnumerator(
      Class<E> enumClass, V value) {
    if (ENUM_CACHE.containsKey(enumClass)) {
      return Optional.ofNullable(CastUtil.cast(ENUM_CACHE.get(enumClass).get(value)));
    }

    E[] enumConstants = enumClass.getEnumConstants();
    Map<Object, Enum<?>> value2Enum = new HashMap<>(enumConstants.length);
    for (E item : enumConstants) {
      value2Enum.put(item.value(), (Enum<?>) item);
    }

    ENUM_CACHE.put(enumClass, value2Enum);
    return Optional.ofNullable(CastUtil.cast(value2Enum.get(value)));
  }
}
