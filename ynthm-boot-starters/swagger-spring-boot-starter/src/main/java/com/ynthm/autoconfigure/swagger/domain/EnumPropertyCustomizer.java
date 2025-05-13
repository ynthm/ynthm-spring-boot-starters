package com.ynthm.autoconfigure.swagger.domain;

import com.fasterxml.jackson.databind.JavaType;
import com.ynthm.common.web.core.enums.Enumerator;
import com.ynthm.common.web.core.util.EnumCache;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.PropertyCustomizer;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class EnumPropertyCustomizer implements PropertyCustomizer {

  private final boolean enable;

  public EnumPropertyCustomizer(boolean enable) {
    this.enable = enable;
  }

  private static Schema<?> getEnumSchema(Schema<?> property, Class<?> enumClass) {
    if (Enumerator.class.isAssignableFrom(enumClass)) {
      List<Enumerator<?>> enumConstants =
          Arrays.stream(enumClass.getEnumConstants())
              .map(i -> (Enumerator<?>) i)
              .collect(Collectors.toList());

      String desc =
          property.getDescription()
              + "   </br>"
              + enumConstants.stream()
                  .map(item -> item.value() + ":" + item.label())
                  .collect(Collectors.joining("; "))
              + "</br>";
      if (EnumCache.valueInt(enumClass)) {
        IntegerSchema integerSchema = new IntegerSchema();
        integerSchema.setEnum(
            enumConstants.stream()
                .map(Enumerator::value)
                .map(i -> (Integer) i)
                .collect(Collectors.toList()));
        integerSchema.setDescription(desc);
        return integerSchema;
      }

      StringSchema stringSchema = new StringSchema();

      stringSchema.setEnum(
          enumConstants.stream()
              .map(Enumerator::value)
              .map(i -> (String) i)
              .collect(Collectors.toList()));
      stringSchema.setDescription(desc);
      return stringSchema;
    }
    return property;
  }

  @Override
  public Schema<?> customize(Schema property, AnnotatedType type) {
    if (!enable) {
      return property;
    }
    if (type.getType() instanceof JavaType) {
      JavaType javaType = (JavaType) type.getType();
      if (javaType.isEnumType()) {
        return getEnumSchema(property, javaType.getRawClass());
      } else if (property.getEnum() != null) {
        return getEnumSchema(
                property,
                AnnotationsUtils.getSchemaAnnotation(type.getCtxAnnotations()).implementation());
      }
    }

    return property;
  }
}
