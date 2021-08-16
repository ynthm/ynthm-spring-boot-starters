package com.ynthm.common;

import com.ynthm.common.exception.BaseException;

/** @author ethan wang */
public interface ResultExceptionAssert extends IResultCode {

  default BaseException newException() {
    return new BaseException(this);
  }

  default BaseException newException(Throwable t) {
    return new BaseException(this, t);
  }

  /**
   * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
   *
   * @param obj 待判断对象
   */
  default void assertNotNull(Object obj) {
    if (obj == null) {
      throw newException();
    }
  }

  /**
   * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
   *
   * @param obj 待判断对象
   */
  default void assertIsNull(Object obj) {
    if (obj != null) {
      throw newException();
    }
  }
}
