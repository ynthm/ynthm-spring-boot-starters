package com.ynthm.common.web.core.enums.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ynthm.common.web.core.enums.Enumerator;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumeratorJsonSerializer extends JsonSerializer<Enumerator<?>> {
  @Override
  public void serialize(Enumerator<?> value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    Serializable valueObject = value.getValue();
    if (valueObject instanceof Integer) {
      gen.writeNumber((Integer) valueObject);
    } else if (valueObject instanceof String) {
      gen.writeString(valueObject.toString());
    }

    JsonStreamContext outputContext = gen.getOutputContext();
    if (outputContext.inObject()) {
      String currentName = outputContext.getCurrentName();
      gen.writeObjectField(currentName + "Label", value.label());
    }
  }
}
