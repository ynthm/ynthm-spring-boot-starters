package com.ynthm.autoconfigure.push.domain.model;

import lombok.Getter;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Getter
public enum DeviceEnum {
  ANDROID("Android"),
  IOS("iOS"),
  WIN_PHONE("WinPhone"),
  QUICK_APP("QuickApp");

  private final String value;

  DeviceEnum(final String value) {
    this.value = value;
  }
}
