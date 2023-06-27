# 说明

## Configure Log4j for Logging

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter</artifactId>
<exclusions>
    <exclusion>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-logging</artifactId>
    </exclusion>
</exclusions>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

## SpringBoot Actuator 监控管理日志

- 好处1：不用编码，只需要引用 Spring Boot Actuator 监控，然后开启访问端点 /loggers，即可轻松查看日志输出级别，并进行切换。
- 好处2：解耦日志框架，无论使用的 logback 还是 log4j2 ，都能轻松切换。

```xml
<!--监控和管理应用程序-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

开启日志访问端点： /loggers

```yaml
management:
  endpoint:
    health:
      show-details: ALWAYS #展示节点的详细信息
  endpoints:
    web:
      exposure:
        include: info,health,logfile,loggers #指定公开的访问端点
```

查看级别

- 发送 GET 请求：http://localhost:8080/actuator/loggers 获取日志等级
- http://localhost:8080/actuator/loggers/x.j.z ：表示指定包或者类的日志输出级别
- 返回的信息非常详细，包含了 ROOT，以及程序中各个包和类的日志级别
- 其中 configuredLevel 表示配置级别，effectiveLevel 表示有效级别，configuredLevel 可能为 null，因为没有配置。

修改日志级别

- post 请求：http://localhost:8080/actuator/loggers/x.j.z，x.j.z 表示指定的包或者类路径，比如修改 root
  全局日志输出级别：http://localhost:8080/actuator/loggers/root
- 请求 Body 的内容格式：{"configuredLevel":"error"}