package com.ynthm.common.web.core.enums.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.ynthm.common.util.CastUtil;
import com.ynthm.common.web.core.enums.Enumerator;
import com.ynthm.common.web.core.util.EnumCache;
import java.io.IOException;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumeratorJsonDeserializer extends JsonDeserializer<Enumerator<?>>
    implements ContextualDeserializer {

  private final Class<? extends Enumerator<?>> classType;

  public EnumeratorJsonDeserializer(Class<? extends Enumerator<?>> valueType) {
    this.classType = valueType;
  }

  @Override
  public Enumerator<?> deserialize(JsonParser jsonParser, DeserializationContext ctx)
      throws IOException {
    if (EnumCache.valueInt(classType)) {
      return EnumCache.getEnumerator(classType, jsonParser.getValueAsInt()).orElse(null);
    } else {
      return EnumCache.getEnumerator(classType, jsonParser.getValueAsString()).orElse(null);
    }
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
    return new EnumeratorJsonDeserializer(CastUtil.cast(ctx.getContextualType().getRawClass()));
  }
}
