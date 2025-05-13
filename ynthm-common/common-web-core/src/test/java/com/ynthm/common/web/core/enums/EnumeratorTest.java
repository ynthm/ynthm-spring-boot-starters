package com.ynthm.common.web.core.enums;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.common.web.core.domain.User;
import com.ynthm.common.web.core.util.EnumCache;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class EnumeratorTest {
  @Test
  void enumCacheTest() {
    Enumerator<?> enumerator = EnumCache.getEnum(Gender.class, 1).orElse(null);
    System.out.println(enumerator.label());
    Gender gender = EnumCache.getEnum(Gender.class, 0).orElse(null);
    System.out.println(gender);

    System.out.println(EnumCache.getEnum(Gender.class, 1).map(Gender::label).orElse(null));
    Optional<Gender> gender2 = EnumCache.getEnumByWildcardClass(Gender.class, 1);
    System.out.println(gender2.map(Gender::label).orElse(null));
  }

  @Test
  void jsonTest() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    String s = objectMapper.writeValueAsString(Gender.MALE);
    Gender gender = objectMapper.readValue(s, Gender.class);
    System.out.println(s);
    System.out.println(gender);
    String userStr =
        objectMapper.writeValueAsString(new User().setGender(Gender.FEMALE).setLevel(Level.BRONZE));
    User user = objectMapper.readValue(userStr, User.class);
    System.out.println(userStr);
    System.out.println(user);
  }
}
