package com.ynthm.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;

/** @author Ethan Wang */
@Slf4j
public class ReflectHelper {

  private ReflectHelper() {}

  public static <T> Class<T> getGenericClass(T object) {
    ParameterizedType parameterizedType =
        (ParameterizedType) object.getClass().getGenericSuperclass();
    return CastUtil.cast(parameterizedType.getActualTypeArguments()[0]);
  }
}
