package com.ynthm.common.web.core.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ynthm.common.api.Label;
import com.ynthm.common.enums.EnumValue;
import com.ynthm.common.web.core.enums.json.EnumeratorJsonDeserializer;
import com.ynthm.common.web.core.enums.json.EnumeratorJsonSerializer;
import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@JsonSerialize(using = EnumeratorJsonSerializer.class)
@JsonDeserialize(using = EnumeratorJsonDeserializer.class)
public interface Enumerator<T extends Serializable>
    extends com.baomidou.mybatisplus.annotation.IEnum<T>, EnumValue<T>, Label {
  /** Struct Mapper 需要 getValue 才能用 @Mapping(source = "status.value", target = "status") */
  @Override
  default T getValue() {
    return value();
  }
}
