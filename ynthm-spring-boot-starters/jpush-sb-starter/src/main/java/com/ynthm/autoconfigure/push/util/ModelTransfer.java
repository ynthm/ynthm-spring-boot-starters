package com.ynthm.autoconfigure.push.util;

import cn.jiguang.common.DeviceType;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.audience.AudienceType;
import com.ynthm.autoconfigure.push.domain.dto.AliasTagsDto;
import com.ynthm.autoconfigure.push.domain.dto.PushDto;
import com.ynthm.autoconfigure.push.domain.model.*;
import com.ynthm.autoconfigure.push.enums.ResultCode4Push;
import com.ynthm.common.domain.Result;

import java.security.InvalidParameterException;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class ModelTransfer {
  private ModelTransfer() {}

  public static Result<PushDto> pushResult(PushResult pushResult) {
    if (pushResult.isResultOK()) {
      return Result.ok(new PushDto(pushResult.msg_id, pushResult.sendno));
    }

    return Result.<PushDto>error(ResultCode4Push.SEND_ERROR)
        .addError(String.valueOf(pushResult.error.getCode()), pushResult.error.getMessage());
  }

  public static Result<Void> defaultResult(DefaultResult defaultResult) {
    if (defaultResult.isResultOK()) {
      return Result.ok();
    }
    return Result.error(ResultCode4Push.SEND_ERROR, defaultResult.toString());
  }

  public static AliasTagsDto tagAliasResult(TagAliasResult tagAliasResult){
    AliasTagsDto dto = new AliasTagsDto();
    dto.setAlias(tagAliasResult.alias);
    dto.setTags(tagAliasResult.tags);
    return dto;
  }

  public static Platform platform(WePlatform wePlatform) {
    Platform.Builder builder = Platform.newBuilder().setAll(wePlatform.isAll());
    if (wePlatform.getDeviceTypes() != null) {
      for (DeviceEnum type : wePlatform.getDeviceTypes()) {
        builder.addDeviceType(deviceType(type));
      }
    }

    return builder.build();
  }

  public static Audience audience(Audiences audiences) {
    Audience.Builder builder =
        Audience.newBuilder().setAll(audiences.isAll()).setFile(audiences.isFile());
    if (audiences.getTargets() != null) {
      for (Target target : audiences.getTargets()) {
        builder.addAudienceTarget(
            AudienceTarget.newBuilder()
                .setAudienceType(audienceType(target.getAudienceType()))
                .addAudienceTargetValues(target.getValues())
                .build());
      }
    }

    return null;
  }

  private static AudienceType audienceType(TargetType type) {
    switch (type) {
      case TAG:
        return AudienceType.TAG;
      case TAG_AND:
        return AudienceType.TAG_AND;
      case TAG_NOT:
        return AudienceType.TAG_NOT;
      case ALIAS:
        return AudienceType.ALIAS;
      case SEGMENT:
        return AudienceType.SEGMENT;
      case ABTEST:
        return AudienceType.ABTEST;
      case REGISTRATION_ID:
        return AudienceType.REGISTRATION_ID;
      case FILE:
        return AudienceType.FILE;
      default:
        throw new InvalidParameterException();
    }
  }

  private static DeviceType deviceType(DeviceEnum deviceEnum) {
    switch (deviceEnum) {
      case IOS:
        return DeviceType.IOS;
      case ANDROID:
        return DeviceType.Android;
      case WIN_PHONE:
        return DeviceType.WinPhone;
      case QUICK_APP:
        return DeviceType.QuickApp;
      default:
        throw new InvalidParameterException();
    }
  }
}
