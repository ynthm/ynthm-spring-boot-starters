package com.ynthm.common.web.core.enums;

import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface Enumerator<T extends Serializable>
    extends com.baomidou.mybatisplus.annotation.IEnum<T>, EnumBase<T> {
  /** Struct Mapper 需要 getValue 才能用 @Mapping(source = "status.value", target = "status") */
  @Override
  default T getValue() {
    return value();
  }
}
