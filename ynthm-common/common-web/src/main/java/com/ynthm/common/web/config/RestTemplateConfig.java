package com.ynthm.common.web.config;

import com.ynthm.common.web.properties.OkHttpProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Ethan Wang
 */
@EnableConfigurationProperties({OkHttpProperties.class})
public class RestTemplateConfig {

  private final OkHttpProperties properties;

  public RestTemplateConfig(OkHttpProperties properties) {
    this.properties = properties;
  }

  /**
   * 声明 RestTemplate
   *
   * <p>可以增加拦截器 restTemplate.setInterceptors(...);
   */
  @Bean
  public RestTemplate httpRestTemplate() {
    return new RestTemplate(httpRequestFactory());
  }

  public ClientHttpRequestFactory httpRequestFactory() {
    return new OkHttp3ClientHttpRequestFactory(okHttpConfigClient());
  }

  /**
   * Http 客户端 可设置代理拦截器
   *
   * @return OkHttpClient
   */
  public OkHttpClient okHttpConfigClient() {
    return new OkHttpClient()
        .newBuilder()
        .connectionPool(pool())
        .connectTimeout(properties.getConnectTimeout(), TimeUnit.SECONDS)
        .readTimeout(properties.getReadTimeout(), TimeUnit.SECONDS)
        .writeTimeout(properties.getWriteTimeout(), TimeUnit.SECONDS)
        .hostnameVerifier((hostname, session) -> true)
        .build();
  }

  public ConnectionPool pool() {
    return new ConnectionPool(
        properties.getMaxIdleConnections(), properties.getKeepAliveDuration(), TimeUnit.SECONDS);
  }
}
