package com.ynthm.starter.rocketmq.util;

import com.ynthm.starter.rocketmq.config.MqConst;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class MqUtil {
  private MqUtil() {
  }


  /**
   * destination formats: `topicName:tags`
   *
   * @param topic 消息所属 topic 的名称
   * @param tags  消息标签，方便服务器过滤使用。目前只支持每个消息设置一个
   */
  public static String buildDestination(String topic, String tags) {
    return tags == null ? topic :
            topic + MqConst.COLON + tags;
  }
}
