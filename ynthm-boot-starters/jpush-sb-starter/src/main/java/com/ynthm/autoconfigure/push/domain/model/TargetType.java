package com.ynthm.autoconfigure.push.domain.model;

import lombok.Getter;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Getter
public enum TargetType {
  TAG("tag"),
  TAG_AND("tag_and"),
  TAG_NOT("tag_not"),
  ALIAS("alias"),
  SEGMENT("segment"),
  ABTEST("abtest"),
  REGISTRATION_ID("registration_id"),
  FILE("file"),
  ;

  private final String value;

  TargetType(final String value) {
    this.value = value;
  }
}
