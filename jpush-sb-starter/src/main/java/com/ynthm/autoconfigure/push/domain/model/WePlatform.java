package com.ynthm.autoconfigure.push.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WePlatform {
  private boolean all;
  private Set<DeviceEnum> deviceTypes;

  private boolean includeIos;
  private boolean includeAndroid;

  public WePlatform addDeviceType(DeviceEnum deviceType) {
    if (null == deviceTypes) {
      deviceTypes = new HashSet<>();
    }
    deviceTypes.add(deviceType);
    return this;
  }

  public static WePlatform all() {
    return WePlatform.builder().all(true).includeAndroid(true).includeIos(true).build();
  }

  public static WePlatform android() {
    return WePlatform.builder().includeAndroid(true).build().addDeviceType(DeviceEnum.ANDROID);
  }

  public static WePlatform ios() {
    return WePlatform.builder().includeIos(true).build().addDeviceType(DeviceEnum.IOS);
  }

  public static WePlatform android_ios() {
    return WePlatform.builder()
        .all(true)
        .includeAndroid(true)
        .includeIos(true)
        .build()
        .addDeviceType(DeviceEnum.ANDROID)
        .addDeviceType(DeviceEnum.IOS);
  }
}
