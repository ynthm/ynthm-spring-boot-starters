package com.ynthm.autoconfigure.push.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@Builder
public class Target {
  private TargetType audienceType;
  private Set<String> values;

  public Target addAudienceTargetValue(String target) {
    if (null == values) {
      values = new HashSet<>();
    }
    values.add(target);
    return this;
  }

  public Target addAudienceTargetValues(Collection<String> targets) {
    if (null == values) {
      values = new HashSet<>();
    }
    for (String value : targets) {
      values.add(value);
    }
    return this;
  }
}
