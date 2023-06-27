package com.ynthm.cloud.user.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@FeignClient(value = "httpbin", url = "https://httpbin.org")
public interface HttpBinClient {

  @GetMapping("/user-agent")
  Map<String, String> userAgent();
}
