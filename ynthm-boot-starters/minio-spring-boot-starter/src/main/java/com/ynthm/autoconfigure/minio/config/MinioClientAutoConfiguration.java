package com.ynthm.autoconfigure.minio.config;

import com.ynthm.autoconfigure.minio.MinioTemplate;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author Ethan Wang
 */
@EnableConfigurationProperties(MinioClientProperties.class)
public class MinioClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(MinioClient.class)
  public MinioClient minioClient(MinioClientProperties minoClientProperties) {

    MinioClient.Builder builder =
        MinioClient.builder()
            .credentials(minoClientProperties.getAccessKey(), minoClientProperties.getSecretKey());
    // endpoint
    int port = minoClientProperties.getPort();
    if (port > 0) {
      builder.endpoint(
          minoClientProperties.getEndpoint(),
          minoClientProperties.getPort(),
          minoClientProperties.isSecure());
    } else {
      builder.endpoint(minoClientProperties.getEndpoint());
    }

    // 设置默认 region
    String region = minoClientProperties.getRegion();
    if (StringUtils.hasText(region)) {
      builder.region(region);
    }

    return builder.build();
  }

  @Bean
  @ConditionalOnBean(MinioClient.class)
  @ConditionalOnMissingBean(MinioTemplate.class)
  public MinioTemplate minioUtil(MinioClient minioClient) {
    return new MinioTemplate(minioClient);
  }
}
