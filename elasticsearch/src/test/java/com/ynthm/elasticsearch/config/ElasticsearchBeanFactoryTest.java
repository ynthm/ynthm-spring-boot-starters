package com.ynthm.elasticsearch.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ElasticsearchBeanFactoryTest {

    @Autowired
    private BeanFactory beanFactory;

    @Test
    void test() {
        Object esClient = beanFactory.getBean("esClient");
        System.out.println(esClient.getClass().getName());
    }
}