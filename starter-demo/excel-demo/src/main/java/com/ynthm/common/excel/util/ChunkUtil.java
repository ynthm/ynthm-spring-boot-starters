package com.ynthm.common.excel.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ChunkUtil {

  public static void main(String[] args) {

    Consumer<List<Integer>> consumer = integers -> System.out.println(integers);

    Stream<Integer> range = IntStream.range(0, 103).boxed();

    int chunkSize = 10;

    ChunkedSpliterator.<Integer>chunkify(range, chunkSize).forEach(consumer);
//    partition(range.boxed(), chunkSize, consumer);
    // partition(range.boxed(), chunkSize).forEach(consumer);
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

  public static <T> Stream<List<T>> partition(Stream<T> stream, int size) {
    Iterator<List<T>> listIterator = Iterators.partition(stream.iterator(), size);
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(listIterator, Spliterator.ORDERED), false);
  }

  public static <T> void partition(Stream<T> stream, int size, Consumer<List<T>> consumer) {
    stream
            .map(createList(size))
            .reduce(processChunks(consumer, size))
            .ifPresent(consumer);
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

  public static <T, V> List<V> partitionCall2ListAsync(
      List<T> dataList,
      int size,
      ExecutorService executorService,
      Function<List<T>, List<V>> function) {
    if (CollectionUtils.isEmpty(dataList)) {
      return new ArrayList<>(0);
    }
    Preconditions.checkArgument(size > 0, "size must not be a minus");

    List<CompletableFuture<List<V>>> completableFutures =
        Lists.partition(dataList, size).stream()
            .map(
                eachList -> {
                  if (executorService == null) {
                    return CompletableFuture.supplyAsync(() -> function.apply(eachList));
                  } else {
                    return CompletableFuture.supplyAsync(
                        () -> function.apply(eachList), executorService);
                  }
                })
            .collect(Collectors.toList());

    CompletableFuture<Void> allFinished =
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
    try {
      allFinished.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return completableFutures.stream()
        .map(CompletableFuture::join)
        .filter(CollectionUtils::isNotEmpty)
        .reduce(
            new ArrayList<V>(),
            ((list1, list2) -> {
              List<V> resultList = new ArrayList<>();
              if (CollectionUtils.isNotEmpty(list1)) {
                resultList.addAll(list1);
              }

              if (CollectionUtils.isNotEmpty(list2)) {
                resultList.addAll(list2);
              }
              return resultList;
            }));
  }
}
