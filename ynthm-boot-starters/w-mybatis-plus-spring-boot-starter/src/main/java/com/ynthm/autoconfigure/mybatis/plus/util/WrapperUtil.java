package com.ynthm.autoconfigure.mybatis.plus.util;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ynthm.autoconfigure.mybatis.plus.config.SqlConstant;
import com.ynthm.autoconfigure.mybatis.plus.domain.Column2Value;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class WrapperUtil {
  private WrapperUtil() {}

  public static <T> boolean exists(
      Class<T> entityClass,
      Predicate<Wrapper<T>> exists,
      List<Column2Value<T>> eqs,
      List<Column2Value<T>> nes) {

    LambdaQueryWrapper<T> queryWrapper =
        Wrappers.lambdaQuery(entityClass).last(SqlConstant.SQL_LIMIT_1);

    for (Column2Value<T> eq : eqs) {
      queryWrapper.eq(eq.getValue() != null, eq.getColumn(), eq.getValue());
    }
    if (nes != null && !nes.isEmpty()) {
      for (Column2Value<T> ne : nes) {
        queryWrapper.ne(ne.getValue() != null, ne.getColumn(), ne.getValue());
      }
    }

    return exists.test(queryWrapper);
  }
}
