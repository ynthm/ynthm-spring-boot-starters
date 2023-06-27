package com.ynthm.common.utils;

import com.ynthm.common.constant.Constant;
import com.ynthm.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author ethan
 */
@Slf4j
public class HttpClientUtil {

  /** 默认超时10s */
  private static final Timeout DEFAULT_TIMEOUT = Timeout.ofSeconds(10);

  /** 连接池 */
  private PoolingHttpClientConnectionManager cm = null;

  private HttpClientUtil() {

    Registry<ConnectionSocketFactory> socketFactoryRegistry;
    try {
      socketFactoryRegistry =
          RegistryBuilder.<ConnectionSocketFactory>create()
              .register("https", sslSocketFactory())
              .register("http", PlainConnectionSocketFactory.INSTANCE)
              .build();
      // 还有很多可以定义的项目
      cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
      cm.setMaxTotal(200);
      cm.setDefaultMaxPerRoute(20);
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      log.error("socket factory registry init error.", e);
    }
  }

  private SSLConnectionSocketFactory sslSocketFactory()
      throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
    return new SSLConnectionSocketFactory(sslContext(), NoopHostnameVerifier.INSTANCE);
  }

  private SSLContext sslContext()
      throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
    return SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
  }

  private SSLContext sslContext(File file, String storePassword)
      throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException,
          KeyManagementException {
    return SSLContextBuilder.create().loadTrustMaterial(file, storePassword.toCharArray()).build();
  }

  private static class SingletonHolder {
    private static final HttpClientUtil UTIL = new HttpClientUtil();
  }

  public static HttpClientUtil instance() {
    return SingletonHolder.UTIL;
  }

  public CloseableHttpClient getHttpClient(Timeout timeout) {

    // 配置请求参数
    RequestConfig requestConfig =
        RequestConfig.custom()
            .setConnectionRequestTimeout(timeout)
            .setConnectTimeout(timeout)
            .setResponseTimeout(timeout)
            .build();

    return HttpClients.custom()
        .setConnectionManager(cm)
        .setDefaultRequestConfig(requestConfig)
        // .setUserTokenHandler((httpRoute, httpContext) -> null)
        .build();
  }

  final RequestConfig defaultRequestConfig =
      RequestConfig.custom()
          .setCookieSpec(StandardCookieSpec.STRICT)
          .setExpectContinueEnabled(true)
          .setConnectionRequestTimeout(Timeout.ofSeconds(5))
          .setConnectTimeout(Timeout.ofSeconds(5))
          .setTargetPreferredAuthSchemes(
              Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
          // .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
          .build();

  private final CloseableHttpClient defaultHttpClient =
      HttpClients.custom()
          .setConnectionManager(cm)
          .setDefaultRequestConfig(defaultRequestConfig)
          // .setUserTokenHandler((httpRoute, httpContext) -> null)
          .build();

  private CloseableHttpClient getDefaultHttpClient() {
    return defaultHttpClient;
  }

  public String get(HttpHost httpHost, String uri) {
    // Create an HttpClient with the given custom dependencies and configuration.
    try (CloseableHttpClient httpClient = getDefaultHttpClient()) {
      final HttpGet httpget = new HttpGet(uri);
      // Request configuration can be overridden at the request level.
      // They will take precedence over the one set at the client level.
      final RequestConfig requestConfig =
          RequestConfig.copy(defaultRequestConfig)
              .setConnectionRequestTimeout(Timeout.ofSeconds(5))
              .setConnectTimeout(Timeout.ofSeconds(5))
              .build();
      httpget.setConfig(requestConfig);

      log.debug("Executing request {} {}", httpget.getMethod(), httpget.getUri());
      return httpClient.execute(httpHost, httpget, responseHandler);
    } catch (IOException | URISyntaxException e) {
      throw new BaseException(e);
    }
  }

  public String proxyGet(HttpHost proxy, String uri) throws IOException {
    try (final CloseableHttpClient httpclient = getDefaultHttpClient()) {
      final HttpGet httpget = new HttpGet(uri);
      // Request configuration can be overridden at the request level.
      // They will take precedence over the one set at the client level.
      final RequestConfig requestConfig =
          RequestConfig.copy(defaultRequestConfig)
              .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
              .setProxy(proxy)
              .build();
      httpget.setConfig(requestConfig);
      return httpclient.execute(httpget, responseHandler);
    }
  }

  public String post(String url, Map<String, String> formParamMap) {
    String result = null;

    HttpPost httppost = new HttpPost(url);
    List<NameValuePair> formParams = new ArrayList<>();

    for (Entry<String, String> nameValuePair : formParamMap.entrySet()) {
      formParams.add(new BasicNameValuePair(nameValuePair.getKey(), nameValuePair.getValue()));
    }

    try (CloseableHttpClient httpclient = getHttpClient(DEFAULT_TIMEOUT);
        UrlEncodedFormEntity uefEntity =
            new UrlEncodedFormEntity(formParams, Constant.CHARSET_UTF_8)) {
      httppost.setEntity(uefEntity);

      log.info("executing request " + httppost.getRequestUri());

      try (CloseableHttpResponse response = httpclient.execute(httppost)) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          result = EntityUtils.toString(entity, Constant.CHARSET_UTF_8);
          log.info("Response content: " + result);
        }
      }

    } catch (IOException | ParseException e) {
      throw new BaseException(e);
    }

    return result;
  }

  /**
   * get请求
   *
   * @param url 请求地址
   * @param params 请求参数
   * @param timeOut 超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
   * @return 响应信息
   */
  public String get(String url, Map<String, String> params, int timeOut) {
    return get(url, params, null, timeOut);
  }

  public String get(
      String url, Map<String, String> params, Map<String, String> headers, int timeOut) {
    return get(url, params, headers, timeOut);
  }

  /** 发送http get请求 */
  public String get(
      String url, Map<String, String> params, Map<String, String> headers, Timeout timeOut) {

    String content = null;

    try {
      // 添加请求参数信息
      URIBuilder uriBuilder = new URIBuilder(url);
      if (null != params) {
        uriBuilder.setParameters(covertParams(params));
      }

      HttpGet httpGet = new HttpGet(uriBuilder.build());
      // 设置header
      if (headers != null) {
        for (Entry<String, String> entry : headers.entrySet()) {
          httpGet.addHeader(entry.getKey(), entry.getValue());
        }
      }

      content = getResult(httpGet, timeOut, false);

    } catch (URISyntaxException e) {
      throw new BaseException(e);
    }

    return content;
  }

  public String postForm(String url, Map<String, String> params, Timeout timeOut) {
    return postForm(url, params, null, Constant.CHARSET_UTF_8, timeOut);
  }

  public String postForm(String url, Map<String, String> params, Map<String, String> headers) {
    return postForm(url, params, headers, Constant.CHARSET_UTF_8, DEFAULT_TIMEOUT);
  }

  /** 发送 http post 请求，参数以form表单键值对的形式提交。 */
  public String postForm(
      String url,
      Map<String, String> params,
      Map<String, String> headers,
      Charset encode,
      Timeout timeOut) {
    if (encode == null) {
      encode = Constant.CHARSET_UTF_8;
    }

    HttpPost httpPost = new HttpPost(url);

    // 添加请求头信息
    if (headers != null) {
      for (Entry<String, String> entry : headers.entrySet()) {
        httpPost.addHeader(entry.getKey(), entry.getValue());
      }
    }
    // 组织请求参数
    if (params != null) {
      httpPost.setEntity(new UrlEncodedFormEntity(covertParams(params), encode));
    }

    return getResult(httpPost, timeOut, false);
  }

  public String postRaw(String url, String stringJson, Map<String, String> headers) {
    return postRaw(url, stringJson, headers, DEFAULT_TIMEOUT);
  }

  public String postRaw(
      String url, String stringJson, Map<String, String> headers, Timeout timeOut) {
    return postRaw(url, stringJson, headers, Constant.CHARSET_UTF_8, timeOut);
  }

  /**
   * 发送 http post 请求，参数以原生字符串进行提交
   *
   * @param url
   * @param encode
   * @return
   */
  public String postRaw(
      String url, String stringJson, Map<String, String> headers, Charset encode, Timeout timeOut) {
    if (encode == null) {
      encode = Constant.CHARSET_UTF_8;
    }

    HttpPost httpost = new HttpPost(url);

    // 设置header
    httpost.setHeader("Content-type", "application/json");
    if (headers != null) {
      for (Entry<String, String> entry : headers.entrySet()) {
        httpost.setHeader(entry.getKey(), entry.getValue());
      }
    }
    // 组织请求参数
    StringEntity stringEntity = new StringEntity(stringJson, encode);
    httpost.setEntity(stringEntity);

    return getResult(httpost, timeOut, false);
  }

  /**
   * 发送 http put 请求，参数以原生字符串进行提交
   *
   * @param url
   * @param encode
   * @return
   */
  public String putRaw(
      String url, String stringJson, Map<String, String> headers, Charset encode, Timeout timeOut) {

    if (encode == null) {
      encode = Constant.CHARSET_UTF_8;
    }

    HttpPut httpput = new HttpPut(url);

    // 设置header
    httpput.setHeader("Content-type", "application/json");
    if (headers != null && headers.size() > 0) {
      for (Entry<String, String> entry : headers.entrySet()) {
        httpput.setHeader(entry.getKey(), entry.getValue());
      }
    }
    // 组织请求参数
    StringEntity stringEntity = new StringEntity(stringJson, encode);
    httpput.setEntity(stringEntity);

    return getResult(httpput, timeOut, false);
  }

  /** 发送http delete请求 */
  public String delete(String url, Map<String, String> headers, Charset encode, Timeout timeout) {

    if (encode == null) {
      encode = Constant.CHARSET_UTF_8;
    }

    HttpDelete httpDelete = new HttpDelete(url);
    if (headers != null) {
      for (Entry<String, String> entry : headers.entrySet()) {
        httpDelete.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return getResult(httpDelete, timeout, false);
  }

  public void doDownload(String url, String targetPath, Map<String, String> header) {
    HttpPost post = new HttpPost(url);
    post.addHeader(HttpHeaders.CONTENT_ENCODING, Constant.UTF_8);
    try (CloseableHttpClient httpclient = getHttpClient(DEFAULT_TIMEOUT)) {
      if (header != null) {
        for (Entry<String, String> entry : header.entrySet()) {
          post.addHeader(entry.getKey(), entry.getValue());
        }
      }

      try (CloseableHttpResponse response = httpclient.execute(post)) {
        if (response.getCode() == 200) {
          Path path = Paths.get(targetPath);
          Files.copy(response.getEntity().getContent(), path);
        } else {
          throw new RuntimeException("System level error, Code=[" + response.getCode() + "].");
        }
      }
    } catch (IOException e) {
      throw new BaseException(e);
    }
  }

  private String getResult(HttpUriRequestBase httpRequest, Timeout timeout, boolean isStream) {
    return getResult(httpRequest, timeout, isStream, null);
  }

  final HttpClientResponseHandler<String> responseHandler =
      response -> {
        final int statusCode = response.getCode();
        if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES) {
          HttpEntity entity = response.getEntity();
          log.info("MineType：" + entity.getContentType());
          return EntityUtils.toString(entity, Constant.CHARSET_UTF_8);
        } // 如果是重定向
        else if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
          return response.getLastHeader("Location").getValue();
        } else {
          // 响应信息
          String reasonPhrase = response.getReasonPhrase();
          log.warn("code[{}],desc[{}]", statusCode, reasonPhrase);
          throw new ClientProtocolException("Unexpected response status: " + statusCode);
        }
      };

  private String getResult(
      HttpUriRequestBase httpRequest,
      Timeout timeout,
      boolean isStream,
      HttpContext clientContext) {
    // 响应结果
    StringBuilder sb = new StringBuilder();

    String responseBody = null;
    BufferedReader br = null;

    try (CloseableHttpClient httpClient = getHttpClient(timeout)) {
      // Create a custom response handler
      // 发起请求
      if (null != clientContext) {
        responseBody = httpClient.execute(httpRequest, clientContext, responseHandler);
      } else {
        responseBody = httpClient.execute(httpRequest, responseHandler);
      }

    } catch (SocketTimeoutException e) {
      log.error("响应超时", e);
    } catch (ClientProtocolException e) {
      log.error("http协议错误", e);
    } catch (UnsupportedEncodingException e) {
      log.error("不支持的字符编码", e);
    } catch (UnsupportedOperationException e) {
      log.error("不支持的请求操作", e);
    } catch (IOException e) {
      log.error("IO错误", e);
    }

    return responseBody;
  }

  /**
   * Map转换成NameValuePair List集合
   *
   * @param params map
   * @return NameValuePair List集合
   */
  private List<NameValuePair> covertParams(Map<String, String> params) {

    List<NameValuePair> paramList = new LinkedList<>();
    for (Entry<String, String> entry : params.entrySet()) {
      paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }

    return paramList;
  }
}
