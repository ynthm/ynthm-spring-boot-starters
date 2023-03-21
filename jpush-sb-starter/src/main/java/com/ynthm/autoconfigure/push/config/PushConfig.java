package com.ynthm.autoconfigure.push.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.ynthm.autoconfigure.push.util.PushTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@EnableConfigurationProperties(PushProperties.class)
public class PushConfig {

  @Bean
  @ConditionalOnMissingBean(PushTemplate.class)
  public PushTemplate jPushClient(PushProperties pushProperties) {
    ClientConfig config = ClientConfig.getInstance();
    config.setMaxRetryTimes(pushProperties.getMaxRetry());
    config.setConnectionTimeout(pushProperties.getConnectionTimeout());
    config.setTimeToLive(pushProperties.getTtl());
    config.setSSLVersion("TLSv1.1");
    config.setApnsProduction(pushProperties.isApnsProduction());
    return new PushTemplate(
        new JPushClient(
            pushProperties.getMasterSecret(), pushProperties.getAppKey(), null, config));
  }
}
