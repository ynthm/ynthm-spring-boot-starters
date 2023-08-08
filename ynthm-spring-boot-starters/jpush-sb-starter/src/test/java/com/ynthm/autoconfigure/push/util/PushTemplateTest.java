package com.ynthm.autoconfigure.push.util;

import cn.jiguang.common.DeviceType;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.AliasDeviceListResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.*;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.ynthm.autoconfigure.push.domain.dto.PushDto;
import com.ynthm.autoconfigure.push.domain.model.Option;
import com.ynthm.autoconfigure.push.domain.model.WePlatform;
import com.ynthm.common.domain.Result;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class PushTemplateTest {
  private JPushClient jPushClient;

  private PushTemplate pushTemplate;

  @BeforeEach
  void setUp() {
    jPushClient = new JPushClient("5e3731de303156910493ecc4", "ec155a4c07db2fad71c6ac1f");
    pushTemplate = new PushTemplate(jPushClient);
  }

  @Test
  void getAliasDeviceList() throws APIConnectionException, APIRequestException {
    // 1a1018970a5bb5f097e 191e35f7e0a5c2d77b2
    AliasDeviceListResult aliasDeviceList =
        jPushClient.getAliasDeviceList("ca6fe172b0e946338edaaf43fa658e01", DeviceType.IOS.value());
    System.out.println(aliasDeviceList.registration_ids);


    Notification.android("hello", "title", new HashMap<>());
  }

  @Test
  void sendNotificationWithAlias() {
    String message = "你好！";
    Map<String, String> extras = new HashMap<>();
    extras.put("message-sender", "ynthm@192.168.3.199/Spark");

    Result<PushDto> result =
        pushTemplate.sendNotificationWithAlias(
            WePlatform.ios(),
            Notification.newBuilder()
                .addPlatformNotification(
                    IosNotification.newBuilder()
                        .setAlert(IosAlert.newBuilder().setTitleAndBody("zhang", null, message).build())
                        .incrBadge(1)
                        .setSound("default")
                        .setContentAvailable(true)
                        .addExtras(extras)
                        .build())
                .build(),
            Option.builder().apnsProduction(true).build(),
            Sets.newHashSet("ca6fe172b0e946338edaaf43fa658e01"));

    System.out.println(result.getCode());
  }
  
  @Test
  void sendAndroidNotificationWithAlias() {
    String message = "hello ethan！！！";
    Map<String, String> extras = new HashMap<>();
    extras.put("123123", "123");
    // Android 通知点击跳转
    JsonObject intent = new JsonObject();
    intent.addProperty(
        "url",
        "intent:#Intent;action=com.apipecloud.ui.activity.OpenClickActivity;component=com.apipecloud/com.apipecloud.ui.activity.OpenClickActivity;end");

    Result<PushDto> result =
        pushTemplate.sendNotificationWithAlias(
            WePlatform.android(),
            Notification.newBuilder()
                .addPlatformNotification(
                    AndroidNotification.newBuilder()
                        .setAlert(message)
                        .addExtras(extras)
                        .setTitle("用户名")
                        .setIntent(intent)
                        .setBadgeAddNum(1)
                        //
                        // .setBadgeClass("com.apipecloud.ui.activity.SplashActivity")
                        //
                        //
                        .setUriAction("com.apipecloud.ui.activity.OpenClickActivity")
                        .build())
                .build(),
            Option.builder().thirdPartyChannel(thirdPartyChannel()).build(),
            Sets.newHashSet("218d105f4f68401eb1926b6f3dce065c"));
    // 15013734208 218d105f4f68401eb1926b6f3dce065c
    System.out.println(result.getCode());
  }

  @Test
  void sendNotificationWithRegistrationId() {
    String message = "hello world！！！";
    Map<String, String> extras = new HashMap<>();
    extras.put("123123", "123");
    // Android 通知点击跳转
    JsonObject intent = new JsonObject();
    intent.addProperty(
            "url",
            "intent:#Intent;action=com.apipecloud.ui.activity.OpenClickActivity;component=com.apipecloud/com.apipecloud.ui.activity.OpenClickActivity;end");

    Result<PushDto> result =
            pushTemplate.sendNotificationWithRegistrationId(
                    WePlatform.android(),
                    Notification.newBuilder()
                            .addPlatformNotification(
                                    AndroidNotification.newBuilder()
                                            .setAlert(message)
                                            .addExtras(extras)
                                            .setTitle("用户名")
//                                            .setIntent(intent)
                                            .setBadgeAddNum(1)
                                            //
                                            // .setBadgeClass("com.apipecloud.ui.activity.SplashActivity")
                                            //
                                            //
//                                            .setUriAction("com.apipecloud.ui.activity.OpenClickActivity")
                                            .build())
                            .build(),
                    Sets.newHashSet("13065ffa4f173217e70"));
    // 15013734208 13065ffa4f173217e70
    System.out.println(result.getCode());
  }

  private Map<String, JsonObject> thirdPartyChannel() {
    Map<String, JsonObject> thirdPartyChannelMap = new HashMap<>();

    JsonObject fcm = new JsonObject();
    // fcm.addProperty("distribution_fcm","secondary_fcm_push");
    fcm.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("fcm", fcm);

    JsonObject huawei = new JsonObject();
    huawei.addProperty("distribution_fcm", "secondary_pns_push");
    huawei.addProperty("distribution", "secondary_push");
    huawei.addProperty("importance", "NORMAL");
    huawei.addProperty("category", "WORK");
    thirdPartyChannelMap.put("huawei", huawei);

    JsonObject xiaomi = new JsonObject();
    xiaomi.addProperty("distribution_fcm", "secondary_pns_push");
    xiaomi.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("xiaomi", xiaomi);

    JsonObject meizu = new JsonObject();
    meizu.addProperty("distribution_fcm", "secondary_pns_push");
    meizu.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("meizu", meizu);

    JsonObject oppo = new JsonObject();
    oppo.addProperty("distribution_fcm", "secondary_pns_push");
    oppo.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("oppo", oppo);

    JsonObject vivo = new JsonObject();
    vivo.addProperty("distribution_fcm", "secondary_pns_push");
    vivo.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("vivo", vivo);

    JsonObject asus = new JsonObject();
    asus.addProperty("distribution_fcm", "secondary_pns_push");
    asus.addProperty("distribution", "secondary_push");
    thirdPartyChannelMap.put("asus", asus);
    return thirdPartyChannelMap;
  }

  @Test
  void sendMessageWithAlias() {}

  @Test
  void sendMessageWithRegistrationId() {
    String message = "hello world！！！";
    Map<String, String> extras = new HashMap<>();
    extras.put("name", "ethan");

    Result<PushDto> result =
            pushTemplate.sendMessageWithRegistrationId(
                    WePlatform.android(),
                    "title",
                    message,
                    Sets.newHashSet("13065ffa4f173217e70"));
    // 15013734208 13065ffa4f173217e70
    System.out.println(result.getCode());
  }

  @Test
  void pushNotifyAndMessage() {}

  @Test
  void bind() {}

  @Test
  void updateDeviceTagAlias() {}

  @Test
  void aliasTags() {}

  @Test
  void pushIos() throws APIConnectionException, APIRequestException {
    String message = "message";
    Map<String, String> extras = new HashMap<>();

    PushPayload payload =
        PushPayload.newBuilder()
            .setPlatform(Platform.ios())
            .setAudience(Audience.all())
            // .setAudience(Audience.registrationId(param.get("id")))
            .setNotification(
                Notification.newBuilder()
                    .addPlatformNotification(
                        IosNotification.newBuilder()
                            .setAlert(message)
                            .incrBadge(1)
                            .setSound(PushTemplate.SOUND_HAPPY)
                            .addExtras(extras)
                            .build())
                    .build())
            .setOptions(Options.newBuilder().setApnsProduction(false).build())
            .setMessage(Message.newBuilder().setMsgContent(message).addExtras(extras).build())
            .build();
    PushResult result = jPushClient.sendPush(payload);
    System.out.println(result);
  }

  /**
   * 所有平台，所有设备，内容为 ALERT 的通知
   *
   * @return
   */
  private PushPayload allAllAlert() {
    return PushPayload.alertAll(PlatformNotification.ALERT);
  }
  /** 所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT。 */
  private PushPayload buildPushObject_all_alias_alert() {
    return PushPayload.newBuilder()
        .setPlatform(Platform.all())
        .setAudience(Audience.alias("alias1"))
        .setNotification(Notification.alert(PlatformNotification.ALERT))
        .build();
  }

  /**
   * 平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
   *
   * @return
   */
  private PushPayload buildPushObject_android_tag_alertWithTitle() {
    return PushPayload.newBuilder()
        .setPlatform(Platform.android())
        .setAudience(Audience.tag("tag1"))
        .setNotification(Notification.android(PlatformNotification.ALERT, "TITLE", null))
        .build();
  }

  /**
   * 构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，角标数字为 5，通知声音为
   * "happy"，并且附加字段 from = "JPush"；消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。APNs
   * 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
   *
   * @return
   */
  private PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
    return PushPayload.newBuilder()
        .setPlatform(Platform.ios())
        .setAudience(Audience.tag_and("tag1", "tag_all"))
        .setNotification(
            Notification.newBuilder()
                .addPlatformNotification(
                    IosNotification.newBuilder()
                        .setAlert(PlatformNotification.ALERT)
                        .setBadge(5)
                        .setSound("happy")
                        .addExtra("from", "JPush")
                        .build())
                .build())
        .setMessage(Message.content("msg_content"))
        .setOptions(Options.newBuilder().setApnsProduction(true).build())
        .build();
  }

  /**
   * 构建推送对象：平台是 Android 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）交（"alias1" 与 "alias2" 的并集），推送内容是 - 内容为
   * MSG_CONTENT 的消息，并且附加字段 from = JPush。
   */
  private PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
    return PushPayload.newBuilder()
        .setPlatform(Platform.android_ios())
        .setAudience(
            Audience.newBuilder()
                .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                .build())
        .setMessage(
            Message.newBuilder().setMsgContent("msg_content").addExtra("from", "JPush").build())
        .build();
  }
}
