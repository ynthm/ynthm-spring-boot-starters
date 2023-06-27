```java
// OkHttp client
org.springframework.cloud.openfeign.FeignAutoConfiguration.OkHttpFeignConfiguration.okHttpClient
// FeignClient
org.springframework.cloud.openfeign.FeignClientFactoryBean.getTarget
// 执行
feign.SynchronousMethodHandler.executeAndDecode
// 如果引入 Spring Cloud LoadBalancer 并且 @FeignClient value 是服务注册名
org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient.execute
```

- https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/
- https://docs.spring.io/spring-cloud-circuitbreaker/docs/current/reference/html/
- https://spring.io/guides/gs/spring-cloud-loadbalancer/
- https://www.baeldung.com/spring-cloud-load-balancer#bd-load-balancing

ServiceInstanceListSupplier  

NacosDiscoveryClient 是 Nacos 对 DiscoveryClient 的实现。委托给 NacosServiceDiscovery 进行服务发现。