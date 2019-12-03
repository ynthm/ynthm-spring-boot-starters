# spring-tools-demo

## macOS Kafka

```sh
brew install kafka
brew install zookeeper

vim /usr/local/etc/kafka/server.properties
```

```properties
# 找到 listeners=PLAINTEXT://:9092 那一行，把注释取消掉。
listeners=PLAINTEXT://localhost:9092
```

如果想以服务的方式启动，那么可以:

```sh
brew services start zookeeper
brew services start kafka
```

如果只是临时启动，可以:

```sh
zkServer start
kafka-server-start /usr/local/etc/kafka/server.properties
```

### 创建Topic

```sh
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
```

### 产生消息

```sh
kafka-console-producer --broker-list localhost:9092 --topic test
>HELLO Kafka
```

### 消费

简单方式:

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --from-beginning
```

如果使用消费组:

```sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic test --group test-consumer1 --from-beginning
```





## Clients

Spring

```xml
<dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <scope>test</scope>
</dependency>
```

```xml
   <!-- Apache Kafka clients -->
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <version>2.3.1</version>
        </dependency>
```

