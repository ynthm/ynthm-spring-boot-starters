package com.ynthm.common.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/** @author ynthm wang */
public class StreamUtil {

  private StreamUtil() {}

  public <K, V extends Comparable<? super V>> Map<K, V> sortByValueReversed(Map<K, V> map) {
    Map<K, V> result = new LinkedHashMap<>();
    map.entrySet().stream()
        .sorted(Map.Entry.<K, V>comparingByValue().reversed())
        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    return result;
  }

  public <K extends Comparable<? super K>, V> Map<K, V> sortByKeyReversed(Map<K, V> map) {
    Map<K, V> result = new LinkedHashMap<>();
    map.entrySet().stream()
        .sorted(Map.Entry.<K, V>comparingByKey().reversed())
        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    return result;
  }

  public <T> Predicate<T> reduce(List<Predicate<T>> list) {
    return list.stream().reduce(x -> true, Predicate::and);
  }
}
