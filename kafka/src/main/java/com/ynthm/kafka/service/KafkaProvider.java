package com.ynthm.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.kafka.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;

/**
 * Author : Ynthm
 */
@Component
public class KafkaProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProvider.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 消息 TOPIC
     */
    private static final String TOPIC = "xiaoha";


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void sendMessage(long orderId, String orderNum, LocalDateTime createTime) throws JsonProcessingException {

        // 构建一个订单类
        Order order = new Order();
        order.setOrderNum("000000001");
        // 发送消息，订单类的 json 作为消息体
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(order));
        // 监听回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void
            onFailure(Throwable throwable) {
                LOGGER.info("## Send message fail ...");
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOGGER.info("## Send message success ...");
            }
        });

    }
}
