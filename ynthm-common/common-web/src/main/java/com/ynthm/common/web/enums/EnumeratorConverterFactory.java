package com.ynthm.common.web.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ethan Wang
 */
public class EnumeratorConverterFactory implements ConverterFactory<String, Enumerator> {

  /** 目标类型与对应转换器的Map */
  private static final Map<Class, Converter> CONVERTER_MAP = new HashMap<>();

  /**
   * 根据目标类型获取相应的转换器
   *
   * @param targetType 目标类型
   * @param <T> CodedEnum的实现类
   * @return
   */
  @Override
  public <T extends Enumerator> Converter<String, T> getConverter(Class<T> targetType) {
    Converter converter = CONVERTER_MAP.get(targetType);
    if (converter == null) {
      converter = new IntegerStrToEnumConverter<>(targetType);
      CONVERTER_MAP.put(targetType, converter);
    }
    return converter;
  }

  /**
   * 将int对应的字符串转换为目标类型的转换器
   *
   * @param <T> 目标类型（CodedEnum的实现类）
   */
  class IntegerStrToEnumConverter<T extends Enumerator> implements Converter<String, T> {
    private Map<String, T> enumMap = new HashMap<>();

    private IntegerStrToEnumConverter(Class<T> enumType) {
      T[] enums = enumType.getEnumConstants();
      for (T e : enums) {
        // 从 code 反序列化回枚举
        enumMap.put(e.value() + "", e);
        // 从枚举字面量反序列回枚
        // 是Spring默认的方案
        // 此处添加可避免下面convert方法抛出IllegalArgumentException异常后被系统捕获再次调用默认方案
        enumMap.put(((Enum) e).name(), e);
      }
    }

    @Override
    public T convert(String source) {
      T result = enumMap.get(source);
      if (result == null) {
        // 抛出该异常后，会调用 spring 的默认转换方案，即使用 枚举字面量进行映射
        throw new IllegalArgumentException("No element matches " + source);
      }
      return result;
    }
  }
}
