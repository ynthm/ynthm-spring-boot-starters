package com.ynthm.common.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.IntConsumer;

class StreamUtilTest {

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
}
