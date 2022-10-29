package com.ynthm.autoconfigure.minio.config;

import com.ynthm.autoconfigure.minio.MinioTemplate;
import com.ynthm.autoconfigure.minio.MinioUtil;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author Ethan Wang
 */
@EnableConfigurationProperties(MinoClientProperties.class)
public class MinioClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(MinioClient.class)
  public MinioClient minioClient(MinoClientProperties minoClientProperties) {

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

    // region
    String region = minoClientProperties.getRegion();
    if (StringUtils.hasText(region)) {
      builder.region(region);
    }

    return builder.build();
  }

  @Bean
  @ConditionalOnBean(MinioClient.class)
  @ConditionalOnMissingBean(MinioUtil.class)
  public MinioUtil minioUtil(MinioClient minioClient) {
    return new MinioUtil(minioClient);
  }

  @Bean
  @ConditionalOnBean(MinioUtil.class)
  @ConditionalOnMissingBean(MinioTemplate.class)
  public MinioTemplate minioTemplate(MinioUtil minioUtil) {
    return new MinioTemplate(minioUtil);
  }
}
