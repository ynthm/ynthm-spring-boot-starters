package com.ynthm.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ChunkedSpliterator<T> implements Spliterator<List<T>> {
  private static final int PROMOTED_CHARACTERISTICS =
      Spliterator.ORDERED
          | Spliterator.DISTINCT
          | Spliterator.SIZED
          | Spliterator.IMMUTABLE
          | Spliterator.CONCURRENT;
  private static final int SELF_CHARACTERISTICS = Spliterator.NONNULL;

  private final Spliterator<T> src;
  private final int chunkSize;

  public ChunkedSpliterator(Spliterator<T> src, int chunkSize) {
    if (chunkSize < 1) {
      throw new IllegalArgumentException("chunkSize must be at least 1");
    }
    this.src = src;
    this.chunkSize = chunkSize;
  }

  public static <E> Stream<List<E>> chunkify(Stream<E> src, int chunkSize) {
    ChunkedSpliterator<E> wrap = new ChunkedSpliterator<>(src.spliterator(), chunkSize);
    return StreamSupport.stream(wrap, src.isParallel());
  }

  @Override
  public boolean tryAdvance(Consumer<? super List<T>> action) {
    List<T> result = new ArrayList<>((int) Math.min(src.estimateSize(), chunkSize));
    for (int i = 0; i < chunkSize; ++i) {
      if (!src.tryAdvance(result::add)) {
        break;
      }
    }
    if (result.isEmpty()) {
      return false;
    }
    action.accept(result);
    return true;
  }

  @Override
  public Spliterator<List<T>> trySplit() {
    Spliterator<T> srcSplit = src.trySplit();
    return srcSplit == null ? null : new ChunkedSpliterator<>(srcSplit, chunkSize);
  }

  @Override
  public long estimateSize() {
    long srcSize = src.estimateSize();
    if (srcSize <= 0L) {
      return 0L;
    }
    if (srcSize == Long.MAX_VALUE) {
      return Long.MAX_VALUE;
    }
    return (srcSize - 1) / chunkSize + 1;
  }

  @Override
  public int characteristics() {
    return (src.characteristics() & PROMOTED_CHARACTERISTICS) | SELF_CHARACTERISTICS;
  }
}
