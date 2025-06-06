package com.ynthm.autoconfigure.mybatis.plus.domain;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Column2Value<T> {
  private SFunction<T, Object> column;
  private Object value;

  public static <T> Column2Value<T> of(SFunction<T, Object> column, Object value) {
    return new Column2Value<>(column, value);
  }
}
