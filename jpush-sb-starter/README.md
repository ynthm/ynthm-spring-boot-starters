# Read Me First

RegistrationID 定义
集成了 JPush SDK 的应用程序在第一次成功注册到 JPush 服务器时，JPush 服务器会给客户端返回一个唯一的该设备的标识 - RegistrationID。JPush SDK 会以广播的形式发送 RegistrationID 到应用程序。

应用程序可以把此 RegistrationID 保存以自己的应用服务器上，然后就可以根据 RegistrationID 来向设备推送消息或者通知。



## 基础术语

| 名词                        | 描述                                                         |
| --------------------------- | ------------------------------------------------------------ |
| AppKey                      | 开发者所创建的应用在极光平台内的唯一标识 ID。                |
| Master Secret               | 用于开发者在调用极光服务 API 时，与 AppKey 配合使用达到鉴权的目的，请保管好 Master Secret 防止外泄。 |
| 三方厂商通道                | Android 生态中手机设备厂商建设的推送通道，如华为、小米和 FCM 等通道，iOS 生态中 Apple 官方建设的 APNs、VoIP 等通道。 |
| 推送平台（platform）        | Android、iOS 、Windows、web 等以操作系统或业务承载平台为主，具备独立生态和用户隔离的平台。 |
| 推送目标（audience）        | 一条通知消息可以被推送到的目标对象，JPush 提供了多种方式，比如：别名、标签、RID、分群、广播等。 |
| 极光 RID（registration_id） | 用户终端设备在极光服务平台的注册 ID，是用户的唯一设备标识 ID，推送目标的别名和标签均建立在 RID 之上。 |
| 设备标签（tag）             | 开发者可为极光 RID 设置标签，通过标签完成对 RID 的快速批量选择和推送。每个极光 RID 可绑定标签无上限，每个标签下可关联的设备无上限。 |
| 设备别名（Alias）           | 开发者可为极光 RID 设置别名，通过别名完成推送。每个极光 RID 仅可绑定 1 个别名，每个别名下最多可绑定 10 个极光 RID。 |
| 用户分群（segment）         | 根据地理位置、活跃度、智能标签、设备属性等将用户分成不同群体，以实现灵活、精准且高效的推送。 |
| 广播（all）                 | 开发者所创建应用在极光服务的全量注册用户，通过广播完成全量用户的推送。 |



## 引用

- https://docs.jiguang.cn/jpush
- https://github.com/jpush/jpush-api-java-client