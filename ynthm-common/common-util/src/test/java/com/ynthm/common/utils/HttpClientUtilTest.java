package com.ynthm.common.utils;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.ChainElement;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class HttpClientUtilTest {
  private final HttpHost localhostHttpHost = new HttpHost("127.0.0.1");
  private final HttpHost httpBinHttpHost = new HttpHost("httpbin.org");

  @Test
  void get1() {
    String s = HttpClientUtil.instance().get(httpBinHttpHost, "/get");
    System.out.println(s);
  }

  @Test
  void get() {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet("http://httpbin.org/get");
      // The underlying HTTP connection is still held by the response object
      // to allow the response content to be streamed directly from the network socket.
      // In order to ensure correct deallocation of system resources
      // the user MUST call CloseableHttpResponse#close() from a finally clause.
      // Please note that if response content is not fully consumed the underlying
      // connection cannot be safely re-used and will be shut down and discarded
      // by the connection manager.
      try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
        System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
        HttpEntity entity1 = response1.getEntity();
        // do something useful with the response body
        // and ensure it is fully consumed
        EntityUtils.consume(entity1);
      } catch (IOException e) {
        e.printStackTrace();
      }

      HttpPost httpPost = new HttpPost("http://httpbin.org/post");
      List<NameValuePair> nvps = new ArrayList<>();
      nvps.add(new BasicNameValuePair("username", "vip"));
      nvps.add(new BasicNameValuePair("password", "secret"));
      httpPost.setEntity(new UrlEncodedFormEntity(nvps));

      try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
        System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
        HttpEntity entity2 = response2.getEntity();
        // do something useful with the response body
        // and ensure it is fully consumed
        EntityUtils.consume(entity2);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void whenSendMultipartRequestUsingHttpClient_thenCorrect()
      throws ClientProtocolException, IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost("http://www.example.com");

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addTextBody("username", "John");
    builder.addTextBody("password", "pass");
    builder.addBinaryBody(
        "file", new File("test.txt"), ContentType.APPLICATION_OCTET_STREAM, "file.ext");

    HttpEntity multipart = builder.build();
    httpPost.setEntity(multipart);

    CloseableHttpResponse response = client.execute(httpPost);
    //    assertThat(response.getCode(), equalTo(200));
    client.close();
  }

  @Test
  void test4() {
    try (final CloseableHttpClient httpclient =
        HttpClients.custom()

            // Add a simple request ID to each outgoing request

            .addRequestInterceptorFirst(
                new HttpRequestInterceptor() {

                  private final AtomicLong count = new AtomicLong(0);

                  @Override
                  public void process(
                      final HttpRequest request,
                      final EntityDetails entity,
                      final HttpContext context)
                      throws HttpException, IOException {
                    request.setHeader("request-id", Long.toString(count.incrementAndGet()));
                  }
                })

            // Simulate a 404 response for some requests without passing the message down to the
            // backend

            .addExecInterceptorAfter(
                ChainElement.PROTOCOL.name(),
                "custom",
                (request, scope, chain) -> {
                  final Header idHeader = request.getFirstHeader("request-id");
                  if (idHeader != null && "13".equalsIgnoreCase(idHeader.getValue())) {
                    final ClassicHttpResponse response =
                        new BasicClassicHttpResponse(HttpStatus.SC_NOT_FOUND, "Oppsie");
                    response.setEntity(new StringEntity("bad luck", ContentType.TEXT_PLAIN));
                    return response;
                  } else {
                    return chain.proceed(request, scope);
                  }
                })
            .build()) {

      for (int i = 0; i < 20; i++) {
        final HttpGet httpget = new HttpGet("http://httpbin.org/get");

        System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

        try (final CloseableHttpResponse response = httpclient.execute(httpget)) {
          System.out.println("----------------------------------------");
          System.out.println(response.getCode() + " " + response.getReasonPhrase());
          System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException e) {
          e.printStackTrace();
        }
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testClientCustomContext() {
    try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
      // Create a local instance of cookie store
      final CookieStore cookieStore = new BasicCookieStore();

      // Create local HTTP context
      final HttpClientContext localContext = HttpClientContext.create();
      // Bind custom cookie store to the local context
      localContext.setCookieStore(cookieStore);

      final HttpGet httpget = new HttpGet("http://httpbin.org/cookies");
      System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

      // Pass local context as a parameter
      try (final CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
        System.out.println("----------------------------------------");
        System.out.println(response.getCode() + " " + response.getReasonPhrase());
        final List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
          System.out.println("Local cookie: " + cookies.get(i));
        }
        EntityUtils.consume(response.getEntity());
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void clientCustomSSL() {
    // Trust standard CA and those trusted by our custom strategy

    try {
      final SSLContext sslcontext =
          SSLContexts.custom()
              .loadTrustMaterial(
                  new TrustStrategy() {

                    @Override
                    public boolean isTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                      final X509Certificate cert = chain[0];
                      return "CN=httpbin.org".equalsIgnoreCase(cert.getSubjectDN().getName());
                    }
                  })
              .build();

      // Allow TLSv1.2 protocol only
      final SSLConnectionSocketFactory sslSocketFactory =
          SSLConnectionSocketFactoryBuilder.create()
              .setSslContext(sslcontext)
              .setTlsVersions(TLS.V_1_2)
              .build();
      final HttpClientConnectionManager cm =
          PoolingHttpClientConnectionManagerBuilder.create()
              .setSSLSocketFactory(sslSocketFactory)
              .build();

      try (CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build()) {

        final HttpGet httpget = new HttpGet("https://httpbin.org/");

        System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

        final HttpClientContext clientContext = HttpClientContext.create();
        try (CloseableHttpResponse response = httpclient.execute(httpget, clientContext)) {
          System.out.println("----------------------------------------");
          System.out.println(response.getCode() + " " + response.getReasonPhrase());
          System.out.println(EntityUtils.toString(response.getEntity()));

          final SSLSession sslSession = clientContext.getSSLSession();
          if (sslSession != null) {
            System.out.println("SSL protocol " + sslSession.getProtocol());
            System.out.println("SSL cipher suite " + sslSession.getCipherSuite());
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (KeyStoreException | ParseException e) {
      e.printStackTrace();
    }
  }

  @Test
  void uploadFile() {
    try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
      final HttpPost httppost =
          new HttpPost("http://localhost:8080" + "/servlets-examples/servlet/RequestInfoExample");

      final FileBody bin = new FileBody(new File("~/Download/abc.txt"));
      final StringBody comment =
          new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

      final HttpEntity reqEntity =
          MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();

      httppost.setEntity(reqEntity);

      System.out.println("executing request " + httppost);
      try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
        System.out.println("----------------------------------------");
        System.out.println(response);
        final HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
          System.out.println("Response content length: " + resEntity.getContentLength());
        }
        EntityUtils.consume(resEntity);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  void test3() {
    try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
      // Start the client
      httpclient.start();

      // Execute request
      SimpleHttpRequest request1 = SimpleHttpRequests.get("http://httpbin.org/get");
      Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
      // and wait until response is received
      SimpleHttpResponse response1 = future.get();
      System.out.println(request1.getRequestUri() + "->" + response1.getCode());

      // One most likely would want to use a callback for operation result
      CountDownLatch latch1 = new CountDownLatch(1);
      SimpleHttpRequest request2 = SimpleHttpRequests.get("http://httpbin.org/get");
      httpclient.execute(
          request2,
          new FutureCallback<SimpleHttpResponse>() {

            @Override
            public void completed(SimpleHttpResponse response2) {
              latch1.countDown();
              System.out.println(request2.getRequestUri() + "->" + response2.getCode());
            }

            @Override
            public void failed(Exception ex) {
              latch1.countDown();
              System.out.println(request2.getRequestUri() + "->" + ex);
            }

            @Override
            public void cancelled() {
              latch1.countDown();
              System.out.println(request2.getRequestUri() + " cancelled");
            }
          });
      latch1.await();

      // In real world one most likely would want also want to stream
      // request and response body content
      CountDownLatch latch2 = new CountDownLatch(1);
      AsyncRequestProducer producer3 = AsyncRequestBuilder.get("http://httpbin.org/get").build();
      AbstractCharResponseConsumer<HttpResponse> consumer3 =
          new AbstractCharResponseConsumer<HttpResponse>() {

            HttpResponse response;

            @Override
            protected void start(HttpResponse response, ContentType contentType)
                throws HttpException, IOException {
              this.response = response;
            }

            @Override
            protected int capacityIncrement() {
              return Integer.MAX_VALUE;
            }

            @Override
            protected void data(CharBuffer data, boolean endOfStream) throws IOException {
              // Do something useful
            }

            @Override
            protected HttpResponse buildResult() throws IOException {
              return response;
            }

            @Override
            public void releaseResources() {}
          };
      httpclient.execute(
          producer3,
          consumer3,
          new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response3) {
              latch2.countDown();
              System.out.println(request2.getRequestUri() + "->" + response3.getCode());
            }

            @Override
            public void failed(Exception ex) {
              latch2.countDown();
              System.out.println(request2.getRequestUri() + "->" + ex);
            }

            @Override
            public void cancelled() {
              latch2.countDown();
              System.out.println(request2.getRequestUri() + " cancelled");
            }
          });
      latch2.await();

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException | IOException e) {
      e.printStackTrace();
    }
  }
}
