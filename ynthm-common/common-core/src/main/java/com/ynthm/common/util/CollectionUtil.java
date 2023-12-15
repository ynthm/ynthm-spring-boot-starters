package com.ynthm.common.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class CollectionUtil {
  private CollectionUtil() {}

  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isNotEmpty(Collection<?> collection) {
    return !isEmpty(collection);
  }

  public static <E, T> T notEmptyTransfer(
      Collection<E> collection, Function<Collection<E>, T> function) {
    if (collection != null && !collection.isEmpty()) {
      return function.apply(collection);
    }
    return null;
  }

  public static <E> void notEmptyConsumer(Collection<E> collection, Consumer<E> action) {
    if (collection != null && !collection.isEmpty()) {
      collection.forEach(action);
    }
  }

  public static <E, F, T> T notEmptyMapAndTransfer(
      Collection<E> collection, Function<E, F> mapper, Function<List<F>, T> transfer) {
    if (collection != null && !collection.isEmpty()) {
      return transfer.apply(collection.stream().map(mapper).collect(Collectors.toList()));
    }

    return null;
  }
}
