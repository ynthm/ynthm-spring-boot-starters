package com.ynthm.common.web.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class JacksonConfig {

  /**
   * JavaScript 中最大的安全整数 <code>2<sup>53</sup> - 1</code>
   *
   * @see <a
   *     href="https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Number/MAX_SAFE_INTEGER">MDN
   *     - MAX_SAFE_INTEGER</a>
   */
  public static final long MAX_SAFE_INTEGER = 0x1FFFFFFFFFFFFFL;
  /**
   * JavaScript 中最小的安全整数 <code>-(2<sup>53</sup> - 1)</code>
   *
   * @see <a
   *     href="https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Number/MIN_SAFE_INTEGER">MDN
   *     - MIN_SAFE_INTEGER</a>
   */
  public static final long MIN_SAFE_INTEGER = -0x1FFFFFFFFFFFFFL;

  /** 解决 JavaScript 在 json 反序列化时 long 类型缺失精度问题 */
  private static final JsonSerializer<Long> SERIALIZER =
      new JsonSerializer<Long>() {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
          if (value == null) {
            gen.writeNull();
            return;
          }
          // 是否是 JavaScript 安全整数
          boolean isSafeInteger = MIN_SAFE_INTEGER <= value && value <= MAX_SAFE_INTEGER;
          if (isSafeInteger) {
            gen.writeNumber(value);
          } else {
            gen.writeString(value.toString());
          }
        }
      };

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return jacksonObjectMapperBuilder ->
        jacksonObjectMapperBuilder
            .featuresToDisable(
                // 反序列化时候遇到不匹配的属性并不抛出异常
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                // 序列化时候遇到空对象不抛出异常
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                // 反序列化的时候如果是无效子类型,不抛出异常
                DeserializationFeature.FAIL_ON_INVALID_SUBTYPE,
                // 不使用默认的 dateTime 进行序列化
                SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
            .modules(new Jdk8Module(), new JavaTimeModule())
            .failOnEmptyBeans(false)
            .serializerByType(Long.class, SERIALIZER)
            .serializerByType(Long.TYPE, ToStringSerializer.instance);
  }
}
