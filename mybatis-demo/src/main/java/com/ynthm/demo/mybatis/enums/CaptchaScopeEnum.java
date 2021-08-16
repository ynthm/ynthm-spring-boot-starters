package com.ynthm.demo.mybatis.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** @author ethan */
@Getter
@AllArgsConstructor
public enum CaptchaScopeEnum {
  REGISTER(0, "user:register:"),
  CHANGE_PASSWORD(1, "change:password:"),
  WITHDRAWAL_COIN(2, "withdrawal:coin:"),
  INVITATION_CUSTOMER(3, "invitation:customer:"),
  INVITATION_AGENT(4, "invitation:agent:");

  @JsonValue private final int scope;
  private final String cacheCode;
}
