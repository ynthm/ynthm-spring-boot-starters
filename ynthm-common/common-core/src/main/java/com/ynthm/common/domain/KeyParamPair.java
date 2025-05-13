package com.ynthm.common.domain;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * 关键参数对
 *
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class KeyParamPair {
  private Map<String, Object> map = Maps.newLinkedHashMap();

  public static KeyParamPair of(String key, Object value) {
    return new KeyParamPair().put(key, value);
  }

  public KeyParamPair put(String key, Object value) {
    map.put(key, value);
    return this;
  }


  public String string() {
    if (map.isEmpty()) {
      return "";
    }

    return " " + map.entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("|"));
  }
}
