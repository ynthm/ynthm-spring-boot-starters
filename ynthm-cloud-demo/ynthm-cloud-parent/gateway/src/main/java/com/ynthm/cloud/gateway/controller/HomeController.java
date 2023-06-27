package com.ynthm.cloud.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@RestController
public class HomeController {

  @RequestMapping("/circuitbreakerfallback")
  public String circuitbreakerfallback() {
    return "This is a fallback";
  }
}
