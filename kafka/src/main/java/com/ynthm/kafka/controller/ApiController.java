package com.ynthm.kafka.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author Ethan Wang */
@RequestMapping("/api")
@RestController
public class ApiController {

  @Autowired private KafkaListenerEndpointRegistry registry;

  /**
   * 开启顺序
   *
   * <p>country_dict gts2symbol customer
   *
   * <p>t_customer_info_bank < t_customer | t_customer_info_file < t_customer | t_account_info <
   * t_customer | gts2account < t_customer
   *
   * <p>gts2deal < t_customer gts2deal < gts2account(activeTime)
   *
   * @param id KafkaListener 也就是 group id
   * @return 结果
   */
  @GetMapping("/listeners/resume/{id}")
  public String resume(@PathVariable String id) {
    MessageListenerContainer listenerContainer = registry.getListenerContainer(id);
    if (listenerContainer != null) {
      if (!listenerContainer.isRunning()) {
        listenerContainer.start();
      }
      listenerContainer.resume();
    }

    return "ok";
  }

  @GetMapping("/listeners/pause/{id}")
  public String pause(@PathVariable String id) {
    MessageListenerContainer listenerContainer = registry.getListenerContainer(id);
    if (listenerContainer != null) {
      listenerContainer.pause();
    }

    return "ok";
  }
}
