package com.ynthm.autoconfigure.push.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagAliasResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.ynthm.autoconfigure.push.domain.args.PushArgs;
import com.ynthm.autoconfigure.push.domain.dto.AliasTagsDto;
import com.ynthm.autoconfigure.push.domain.dto.PushDto;
import com.ynthm.autoconfigure.push.domain.model.Audiences;
import com.ynthm.autoconfigure.push.domain.model.Option;
import com.ynthm.autoconfigure.push.domain.model.WePlatform;
import com.ynthm.common.domain.Result;
import com.ynthm.common.exception.UtilException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class PushTemplate implements PushOperations {

  public static final String SOUND_HAPPY = "happy";

  private final JPushClient client;

  public PushTemplate(JPushClient client) {
    this.client = client;
  }

  @Override
  public Result<PushDto> sendNotificationWithAlias(
      WePlatform platform, Notification notification, Option option, Collection<String> aliases) {
    Options.Builder opBuilder = Options.newBuilder();

    if (platform.isIncludeIos()) {
      opBuilder.setApnsProduction(option.isApnsProduction());
    } else if (platform.isIncludeAndroid()) {
      opBuilder.setThirdPartyChannelV2(option.getThirdPartyChannel());
    }

    try {
      PushPayload payload =
          PushPayload.newBuilder()
              .setPlatform(ModelTransfer.platform(platform))
              .setAudience(Audience.alias(aliases))
              .setNotification(notification)
              .setOptions(opBuilder.build()).setMessage(Message.newBuilder().build())
              .build();

      PushResult result = client.sendPush(payload);
      log.info("send notification with alias: {}", result);
      return ModelTransfer.pushResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  @Override
  public Result<PushDto> sendNotificationWithRegistrationId(
      WePlatform platform, Notification notification, Collection<String> registrationId) {
    try {
      PushPayload payload =
          PushPayload.newBuilder()
              .setPlatform(ModelTransfer.platform(platform))
              .setAudience(Audience.registrationId(registrationId))
              .setNotification(notification)
              .build();

      PushResult result = client.sendPush(payload);
      log.info("send notification with rid: {}", result);
      return ModelTransfer.pushResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  @Override
  public Result<PushDto> sendMessageWithAlias(
      WePlatform platform, String title, String msgContent, Collection<String> aliases) {
    try {
      PushPayload payload =
          PushPayload.newBuilder()
              .setPlatform(ModelTransfer.platform(platform))
              .setAudience(Audience.alias(aliases))
              .setMessage(Message.newBuilder().setTitle(title).setMsgContent(msgContent).build())
              .build();
      PushResult result = client.sendPush(payload);
      log.info("send message with alias: {}", result);
      return ModelTransfer.pushResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  @Override
  public Result<PushDto> sendMessageWithRegistrationId(
      WePlatform platform, String title, String msgContent, Collection<String> registrationId) {
    try {
      PushPayload payload =
          PushPayload.newBuilder()
              .setPlatform(ModelTransfer.platform(platform))
              .setAudience(Audience.registrationId(registrationId))
              .setMessage(Message.newBuilder().setTitle(title).setMsgContent(msgContent).build())
              .build();
      PushResult result = client.sendPush(payload);
      log.info("send message with rid: {}", result);
      return ModelTransfer.pushResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  @Override
  public Result<PushDto> pushNotifyAndMessage(
      WePlatform platform, Audiences audiences, PushArgs req) {
    String message = req.getMessage();
    // 推送的关键,构造一个 payload
    PushPayload payload =
        PushPayload.newBuilder()
            .setPlatform(ModelTransfer.platform(platform))
            .setAudience(ModelTransfer.audience(audiences))
            .setNotification(Notification.android(message, req.getTitle(), req.getExtras()))
            .setOptions(Options.newBuilder().setApnsProduction(false).build())
            .setMessage(Message.content(message))
            .build();
    try {
      PushResult result = client.sendPush(payload);
      log.info("send notify and message: {}", result);
      return ModelTransfer.pushResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 绑定手机号
   *
   * @param registrationId
   * @param mobile 手机号码
   */
  @Override
  public Result<Void> bind(String registrationId, String mobile) {
    try {
      DefaultResult result = client.bindMobile(registrationId, mobile);
      log.info("bind registration ID {} : {}", registrationId, result);
      return ModelTransfer.defaultResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 设置 alias 与 tags
   *
   * @param registrationId 注册ID
   * @param alias 别名
   * @param tagsToAdd
   * @param tagsToRemove
   * @return
   */
  @Override
  public Result<Void> updateDeviceTagAlias(
      String registrationId, String alias, Set<String> tagsToAdd, Set<String> tagsToRemove) {
    try {
      DefaultResult result =
          client.updateDeviceTagAlias(registrationId, alias, tagsToAdd, tagsToRemove);
      log.info("update device tags and alias {} : {}", registrationId, result);
      return ModelTransfer.defaultResult(result);
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }

  /** 获取Tag Alias */
  @Override
  public Result<AliasTagsDto> aliasTags(String registrationId) {
    try {
      TagAliasResult result = client.getDeviceTagAlias(registrationId);
      log.info(
          "registrationId: {} alias: {} tags: {}",
          registrationId,
          result.alias,
          result.tags.toString());
      return Result.ok(ModelTransfer.tagAliasResult(result));
    } catch (APIConnectionException | APIRequestException e) {
      throw new UtilException(e);
    }
  }
}
