package com.ynthm.excel.demo.redis.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.ynthm.common.web.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;

/** @author Ethan Wang */
@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

  @Autowired private RedisSlidingWindowLua redisSlidingWindowLua;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;

      Method method = handlerMethod.getMethod();
      //      if (!method.isAnnotationPresent(AccessLimit.class)) {
      //        return true;
      //      }
      AccessLimit methodAnnotation = method.getAnnotation(AccessLimit.class);

      AccessLimit classAnnotation = method.getDeclaringClass().getAnnotation(AccessLimit.class);
      AccessLimit accessLimit = methodAnnotation != null ? methodAnnotation : classAnnotation;
      if (accessLimit == null) {
        return true;
      }

      int limit = accessLimit.limit();
      int seconds = accessLimit.seconds();

      String key = request.getRequestURI().replace("/", ":");
      // ip or session id
      String ip = ServletUtil.getIpAddr(request);
      ArrayList<String> keys = CollUtil.newArrayList(key + ":" + ip);

      if (redisSlidingWindowLua.block(keys, limit, seconds)) {
        ServletUtil.renderString(response, "请求太频繁!");
        return false;
      }
    }

    return true;
  }
}
