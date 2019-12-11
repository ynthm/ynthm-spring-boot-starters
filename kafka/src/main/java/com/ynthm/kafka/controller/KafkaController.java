package com.ynthm.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ynthm.kafka.domain.Order;
import com.ynthm.kafka.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @RequestMapping("/producer")
    public void producer(@RequestBody Order order) throws JsonProcessingException {

        kafkaProducer.sendMessage(order);

    }
}
