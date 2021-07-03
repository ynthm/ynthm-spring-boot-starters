package com.ynthm.spring.web.demo.web;

import cn.hutool.core.util.StrUtil;
import com.ynthm.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** @author ynthm */
@Slf4j
public class ServletUtil {

  public static final String UNKNOWN = "unknown";

  private ServletUtil() {}

  public static void renderString(HttpServletResponse response, String json) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(Constant.UTF_8);
    try (PrintWriter pw = response.getWriter()) {
      pw.write(json);
    }
  }

  /**
   * @param response 响应
   * @param fileName 带扩展名的文件名
   */
  public static void responseForDownload(HttpServletResponse response, String fileName) {
    response.setContentType(
        MediaTypeFactory.getMediaType(fileName).orElse(MediaType.ALL).toString());
    response.setCharacterEncoding(Constant.UTF_8);
    try {
      // 这里URLEncoder.encode可以防止中文乱码
      fileName = URLEncoder.encode(fileName, Constant.UTF_8);
    } catch (UnsupportedEncodingException e) {
      log.error("encoding filename error.", e);
    }

    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
  }

  public static void exception4Download(HttpServletResponse response, String resultJson)
      throws IOException {
    // 重置response
    response.reset();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(Constant.UTF_8);
    try (PrintWriter pw = response.getWriter()) {
      pw.println(resultJson);
    }
  }

  public static String getIpAddr(HttpServletRequest request) {
    String ip = null;
    try {
      // X-Forwarded-For: client1, proxy1, proxy2, proxy3
      ip = request.getHeader("x-forwarded-for");
      if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (StrUtil.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
      }
      if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
      if ("0:0:0:0:0:0:0:1".equals(ip)) {
        ip = "127.0.0.1";
      }
    } catch (Exception e) {
      log.error("ERROR", e);
    }

    // 使用代理，则获取第一个IP地址
    if (StrUtil.isNotEmpty(ip) && ip.length() > 15) {
      if (ip.indexOf(',') > -1) {
        ip = ip.substring(0, ip.indexOf(','));
      }
    }
    return ip;
  }

  /**
   * 获取User-Agent
   *
   * @return User-Agent
   */
  public static String getUserAgent(HttpServletRequest request) {
    return request.getHeader(HttpHeaders.USER_AGENT);
  }

  /**
   * nginx location 处加上proxy_set_header X-Forwarded-Proto $scheme;
   *
   * @param request 请求
   * @return base url
   */
  public static String getBaseUrl(HttpServletRequest request) {

    String scheme = request.getHeader("X-Forwarded-Proto");
    if (StrUtil.isBlank(scheme)) {
      scheme = request.getScheme();
    }
    return scheme
        + "://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath();
  }

  public static String getReferer(HttpServletRequest request) {
    String url = request.getHeader(HttpHeaders.REFERER);
    if (StrUtil.isBlank(url)) {
      url = request.getRequestURL().toString();
    }
    return url;
  }
}
