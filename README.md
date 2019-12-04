# spring-tools-demo

## macOS Kafka

```sh
# 安装kafka前默认安装了zookeeper
brew install kafka
# brew install zookeeper

vim /usr/local/etc/kafka/server.properties
```

```properties
# 找到 listeners=PLAINTEXT://:9092 那一行，把注释取消掉。
listeners=PLAINTEXT://localhost:9092
```

如果想以服务的方式启动，那么可以:

```sh
brew services start kafka
brew services start zookeeper
```

如果只是临时启动，可以:

```sh
# 使用配置 /usr/local/etc/zookeeper/zoo.cfg
zkServer start
kafka-server-start /usr/local/etc/kafka/server.properties

zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties

zookeeper-server-stop
kafka-server-stop
```

### 创建Topic

```sh
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic test
# 数据默认存储在 log.dirs=/usr/local/var/lib/kafka-logs
# 这里--replication-factor 不大于集群的 brokers  会有 test-0 test-1文件夹
```

### 产生消息

```sh
# 新建一个 SHELL 
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

可视化 UI

```sh
brew cask install kafka-tool
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

