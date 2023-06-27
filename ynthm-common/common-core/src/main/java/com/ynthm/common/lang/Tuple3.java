package com.ynthm.common.lang;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Getter
@EqualsAndHashCode
public class Tuple3<T1 extends Serializable, T2 extends Serializable, T3 extends Serializable>
    extends Tuple2<T1, T2> implements Serializable {

  private final T3 t3;

  public Tuple3(T1 t1, T2 t2, T3 t3) {
    super(t1, t2);
    this.t3 = t3;
  }

  public static <T1 extends Serializable, T2 extends Serializable, T3 extends Serializable>
      Tuple3 of(T1 t1, T2 t2, T3 t3) {
    return new Tuple3(t1, t2, t3);
  }

  public <R extends Serializable> Tuple3<R, T2, T3> mapT1(Function<T1, R> mapper) {
    return new Tuple3(mapper.apply(this.t1), this.t2, this.t3);
  }

  public <R extends Serializable> Tuple3<T1, R, T3> mapT2(Function<T2, R> mapper) {
    return new Tuple3(this.t1, mapper.apply(this.t2), this.t3);
  }

  public <R extends Serializable> Tuple3<T1, T2, R> mapT3(Function<T3, R> mapper) {
    return new Tuple3(this.t1, this.t2, mapper.apply(this.t3));
  }
}
