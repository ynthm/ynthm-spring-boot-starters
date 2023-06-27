package com.ynthm.common.web.core.util;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class BeanCopierUtil {
  private BeanCopierUtil() {}
  /** 创建过的BeanCopier实例放到缓存中，下次可以直接获取，提升性能 */
  private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

  public static void copy(Object srcObj, Object destObj, Converter converter) {
    BeanCopier copier = getBeanCopier(srcObj.getClass(), destObj.getClass(), converter);
    copier.copy(srcObj, destObj, converter);
  }

  public static <S, T> List<T> listCopyProperties(List<S> sourceList, Supplier<T> target) {
    return listCopyProperties(sourceList, target, null);
  }

  public static <S, T> List<T> listCopyProperties(
      List<S> sourceList, Supplier<T> target, Converter converter) {
    List<T> list = new ArrayList<>(sourceList.size());
    for (S source : sourceList) {
      T t = target.get();
      copy(source, t, converter);
      list.add(t);
    }

    return list;
  }

  private static BeanCopier getBeanCopier(
      Class<?> sourceClass, Class<?> targetClass, Converter converter) {
    String beanKey = generateKey(sourceClass, targetClass, converter);

    return BEAN_COPIERS.computeIfAbsent(
        beanKey, k -> BeanCopier.create(sourceClass, targetClass, true));
  }

  private static String generateKey(Class<?> srcClazz, Class<?> destClazz, Converter converter) {
    return srcClazz.getName() + destClazz.getName() + converter.getClass().getName();
  }
}
