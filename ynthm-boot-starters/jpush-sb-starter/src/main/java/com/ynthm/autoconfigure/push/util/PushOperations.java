package com.ynthm.autoconfigure.push.util;

import cn.jpush.api.push.model.notification.Notification;
import com.ynthm.autoconfigure.push.domain.args.PushArgs;
import com.ynthm.autoconfigure.push.domain.dto.AliasTagsDto;
import com.ynthm.autoconfigure.push.domain.dto.PushDto;
import com.ynthm.autoconfigure.push.domain.model.Audiences;
import com.ynthm.autoconfigure.push.domain.model.Option;
import com.ynthm.autoconfigure.push.domain.model.WePlatform;
import com.ynthm.common.domain.Result;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface PushOperations {
  Result<PushDto> sendNotificationWithAlias(
      WePlatform platform, Notification notification, Option option, Collection<String> aliases);

  Result<PushDto> sendNotificationWithRegistrationId(
      WePlatform platform, Notification notification, Collection<String> registrationId);

  Result<PushDto> sendMessageWithAlias(
      WePlatform platform, String title, String msgContent, Collection<String> aliases);

  /**
   * 发送自定义消息
   *
   * @param platform
   * @param title
   * @param msgContent
   * @param registrationId
   * @return
   */
  Result<PushDto> sendMessageWithRegistrationId(
      WePlatform platform, String title, String msgContent, Collection<String> registrationId);

  Result<PushDto> pushNotifyAndMessage(WePlatform platform, Audiences audiences, PushArgs req);

  /**
   * 绑定手机号
   *
   * @param registrationId
   * @param mobile 手机号码
   */
  Result<Void> bind(String registrationId, String mobile);

  /**
   * 设置 alias 与 tags
   *
   * @param registrationId 注册ID
   * @param alias 别名
   * @param tagsToAdd
   * @param tagsToRemove
   * @return
   */
  Result<Void> updateDeviceTagAlias(
      String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove);

  /** 获取Tag Alias */
  Result<AliasTagsDto> aliasTags(String registrationId);
}
