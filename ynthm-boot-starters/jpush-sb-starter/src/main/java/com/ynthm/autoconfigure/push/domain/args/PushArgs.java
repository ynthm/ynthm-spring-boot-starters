package com.ynthm.autoconfigure.push.domain.args;

import lombok.Data;

import java.util.Map;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class PushArgs {
  /** 指定用户 */
  private String registrationId;

  private String title;
  private String message;
  private Map<String, String> extras;
}
