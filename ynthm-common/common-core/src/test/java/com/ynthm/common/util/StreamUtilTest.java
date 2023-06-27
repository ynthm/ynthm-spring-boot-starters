package com.ynthm.common.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class StreamUtilTest {

  @Test
  void sortByValueReversed() {}

  @Test
  void sortByKeyReversed() {}

  @Test
  void reduce() {}

  @Test
  void arr() {

    int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    Spliterator.OfInt spliterator0 = Arrays.spliterator(arr);
    // trySplit()  将剩余元素分成两部分，取出第一部分
    Spliterator<Integer> spliterator1 = spliterator0.trySplit();
    Spliterator<Integer> spliterator2 = spliterator1.trySplit();

    System.out.println(spliterator0.getExactSizeIfKnown());
    System.out.println(spliterator1.getExactSizeIfKnown());
    System.out.println(spliterator2.getExactSizeIfKnown());

    System.out.println(spliterator0.characteristics());
    System.out.println(spliterator1.characteristics());
    System.out.println(spliterator2.characteristics());

    // estimateSize 函数获取剩余还没有进行accept操作的元素的数量
    System.out.println(spliterator0.estimateSize());
    System.out.println(spliterator1.estimateSize());
    System.out.println(spliterator2.estimateSize());

    spliterator0.forEachRemaining(
        (IntConsumer) t -> System.out.println(Thread.currentThread().getName() + "-" + t + " "));
    spliterator1.forEachRemaining(
        t -> System.out.println(Thread.currentThread().getName() + "-" + t + " "));
    spliterator2.forEachRemaining(
        t -> System.out.println(Thread.currentThread().getName() + "-" + t + " "));
  }

  @Test
  void chunkify() {
    Consumer<List<Integer>> consumer = integers -> System.out.println(integers);

    Stream<Integer> range = IntStream.range(0, 103).boxed();

    int chunkSize = 10;

    ChunkedSpliterator.chunkify(range, chunkSize).forEach(consumer);
  }

  public <T> Stream<List<T>> chunk(Stream<T> stream, int size) {
    Iterator<T> iterator = stream.iterator();
    Iterator<List<T>> listIterator =
        new Iterator<List<T>>() {
          @Override
          public boolean hasNext() {
            return iterator.hasNext();
          }

          @Override
          public List<T> next() {
            List<T> result = new ArrayList<>(size);
            for (int i = 0; i < size && iterator.hasNext(); i++) {
              result.add(iterator.next());
            }
            return result;
          }
        };

    return StreamSupport.stream(((Iterable<List<T>>) () -> listIterator).spliterator(), false);
  }

  public static <T> void partition(Stream<T> stream, int size, Consumer<List<T>> consumer) {
    stream.map(createList(size)).reduce(processChunks(consumer, size)).ifPresent(consumer);
  }

  private static <T> BinaryOperator<List<T>> processChunks(
      Consumer<List<T>> consumer, int chunkSize) {
    return (data, element) -> {
      if (data.size() < chunkSize) {
        data.addAll(element);
        return data;
      } else {
        consumer.accept(data);
        return element; // in fact it's new data list
      }
    };
  }

  private static <T> Function<T, List<T>> createList(int chunkSize) {
    AtomicInteger limiter = new AtomicInteger(0);
    return element -> {
      limiter.incrementAndGet();
      if (limiter.get() == 1) {
        ArrayList<T> list = new ArrayList<>(chunkSize);
        list.add(element);
        return list;
      } else if (limiter.get() == chunkSize) {
        limiter.set(0);
      }
      return Collections.singletonList(element);
    };
  }
}
