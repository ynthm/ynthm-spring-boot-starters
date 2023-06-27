package com.ynthm.common.lang;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Tuple2<T1 extends Serializable, T2 extends Serializable> implements Serializable {
  protected final T1 t1;
  protected final T2 t2;

  /**
   * 构建{@link Tuple2}对象
   *
   * @param <T1> 键类型
   * @param <T2> 值类型
   * @param t1 第一个值
   * @param t2 第二个值
   * @return {@link Tuple2}
   */
  public static <T1 extends Serializable, T2 extends Serializable> Tuple2<T1, T2> of(T1 t1, T2 t2) {
    return new Tuple2<>(t1, t2);
  }

  public <R extends Serializable> Tuple2<R, T2> mapT1(Function<T1, R> mapper) {
    return new Tuple2(mapper.apply(this.t1), this.t2);
  }

  public <R extends Serializable> Tuple2<T1, R> mapT2(Function<T2, R> mapper) {
    return new Tuple2(this.t1, mapper.apply(this.t2));
  }
}
