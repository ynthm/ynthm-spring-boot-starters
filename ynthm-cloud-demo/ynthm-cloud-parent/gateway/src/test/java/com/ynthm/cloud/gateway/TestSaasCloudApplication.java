//package com.ynthm.cloud.gateway;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.context.annotation.Bean;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.MySQLContainer;
//
//@TestConfiguration(proxyBeanMethods = false)
//class TestSaasCloudApplication {
//
//  @Bean
//  @ServiceConnection
//  MySQLContainer<?> mysqlContainer() {
//    return new MySQLContainer<>("mysql:latest");
//  }
//
//  @Bean
//  @ServiceConnection(name = "redis")
//  GenericContainer<?> redisContainer() {
//    return new GenericContainer<>("redis:latest").withExposedPorts(6379);
//  }
//
//  public static void main(String[] args) {
//    SpringApplication.from(GatewayApplication::main).with(TestSaasCloudApplication.class).run(args);
//  }
//}
