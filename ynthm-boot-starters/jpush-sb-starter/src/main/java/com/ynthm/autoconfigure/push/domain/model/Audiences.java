package com.ynthm.autoconfigure.push.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Builder
@Data
public class Audiences {
  private boolean all;
  private boolean file;
  private Set<Target> targets;

  public Audiences addTarget(Target target) {
    if (null == targets) {
      targets = new HashSet<>();
    }
    targets.add(target);
    return this;
  }
}
