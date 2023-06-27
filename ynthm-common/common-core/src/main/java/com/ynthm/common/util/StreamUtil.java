package com.ynthm.common.util;

import com.google.common.collect.Iterators;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author ynthm wang
 */
public class StreamUtil {

  private StreamUtil() {}

  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueReversed(Map<K, V> map) {
    Map<K, V> result = new LinkedHashMap<>();
    map.entrySet().stream()
        .sorted(Map.Entry.<K, V>comparingByValue().reversed())
        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    return result;
  }

  public static <K extends Comparable<? super K>, V> Map<K, V> sortByKeyReversed(Map<K, V> map) {
    Map<K, V> result = new LinkedHashMap<>();
    map.entrySet().stream()
        .sorted(Map.Entry.<K, V>comparingByKey().reversed())
        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    return result;
  }

  public static <T> Predicate<T> reduce(List<Predicate<T>> list) {
    return list.stream().reduce(x -> true, Predicate::and);
  }

  public static <T> Stream<List<T>> partition(Stream<T> stream, int size) {
    Iterator<List<T>> listIterator = Iterators.partition(stream.iterator(), size);
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(listIterator, Spliterator.ORDERED), false);
  }

  public static <T> Stream<T> stream(Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
  }
}
