package com.ynthm.common.web.core.util;

import com.google.common.io.ByteStreams;
import com.ynthm.common.exception.BaseException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Ethan Wang
 */
public class CacheHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private final byte[] body;

  public CacheHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);
    try {
      body = ByteStreams.toByteArray(request.getInputStream());
    } catch (IOException ex) {
      throw new BaseException(ex);
    }
  }

  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {

    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        // 空
      }

      @Override
      public int read() throws IOException {
        return byteArrayInputStream.read();
      }
    };
  }

  public String getBodyString() {
    return new String(body, StandardCharsets.UTF_8);
  }
}
