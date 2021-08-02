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



AckMode模式	作用
MANUAL	当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后, 手动调用Acknowledgment.acknowledge()后提交
MANUAL_IMMEDIATE	手动调用Acknowledgment.acknowledge()后立即提交
RECORD	当每一条记录被消费者监听器（ListenerConsumer）处理之后提交
BATCH	当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后提交
TIME	当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，距离上次提交时间大于TIME时提交
COUNT	当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
COUNT_TIME	TIME或COUNT　有一个条件满足时提交

`spring.kafka.consumer.enable-auto-commit` 修改成 `false`
`spring.kafka.listener.ack-mode` 修改成
      |- `manual`: 表示手动提交，但是测试下来发现是批量提交
      |- `manual_immediate`: 表示手动提交，当调用 `Acknowledgment#acknowledge`之后立马提交。



**Safe Producer** **配置总结**

上面介绍了创建一个safe producer 所需的配置，下面我们总结一下在不同版本的Kafka中所需要做的配置：

**Kafka < 0.11**

- ack=all (procuder level)：确保在发送ack前，数据已经正常备份
- min.insync.replicas=2 (broker/topic level)：确保至少有两个in ISR 的brokers 有数据后再回送ack
- retires=MAX_INT (producer level)：确保在发生瞬时问题时，可以无限次重试
- max.in.flight.requests.per.connection=1 (producer level)：确保每次仅有一个请求发送，防止在重试时产生乱序数据

 

**Kafka >= 0.11**

- enable.idempotence=true (producer level) + min.insync.replicas=2 (broker/topic level)
  - 隐含的配置为 acks=all, retries=MAX_INT, max.in.flight.requests.per.connection=5 (default)
  - 可以在保证消息顺序的同时，提高performance

这里必须要提到的是：运行一个“safe producer”可能会影响系统的throughput与latency，所以在应用到生产系统前，必须先做测试以判断影响。

**幂等 Idempotent Producer**

```java
public class ProducerConfig extends AbstractConfig {
	public static final String ENABLE_IDEMPOTENCE_CONFIG = "enable.idempotence";
}
```





**High Throughput Producer**

在有大量消息需要发送的情况下，默认的Kafka Producer配置可能无法达到一个可观的的吞吐。在这种情况下，我们可以考虑调整两个方面，以提高Producer 的吞吐。分别为消息压缩（message compression），以及消息批量发送（batching）

控制压缩的参数为 compression.type，可选值为 none（默认），gzip，lz4，snappy

控制batch行为的参数有两个，分别为linger.ms、batch.size。

linger.ms：

- Linger.ms：在发送一个batch出去前，一个Producer等待的毫秒数。默认为0，也就是说Kafka会立即发送一个batch
- 若是引入一些延迟（例如linger.ms=5），则消息以batch形式被发送的概率会增加
- 所以在引入了一点延迟成本后，我们可以增加producer的吞吐以及压缩性能，让producer更高效
- 如果一个batch在linger.ms时间到达之前就满了（由batch.size控制），则这个batch会被立即发送到Kafka。所以不需要担心过长的等待时间。

batch.size：

- batch.size：在一个batch中，最多能容纳的字节数。默认为16KB
- 在大多数情况下，增加此参数到32KB或64KB可以有效提高压缩、吞吐、以及请求的性能
- 任何超过此batch size大小的消息不会被batch
- batch的分配基于partition数目，所以确保不要设置太高的值，以防止使用过多内存
- 我们可以使用Kafka Producer Metrics监控average batch size 指标

```java
// high throughput producer at the expense of a lit bit latency and CPU usage
properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // 32 KB batch size
```

在配置以上参数后，发送给Kafka的消息即为压缩后的消息。不过在Consumer中，不需要做任何配置即可正常读取并将这些消息转回文本。



**Max.block.ms & buffer.memory**

如果一个Producer 发送消息的速度超出了broker可以处理的速度，则records会被buffer在内存中：

- buffer.memory = 33554432（32MB）即为send buffer的默认大小
- 此buffer会随着时间的增加而填满，并随着broker吞吐增加后，buffer数据量减少

如果buffer满了（所有32MB都被占用），则 .send() 方法会被阻塞（也就是说，Producer不会再生产更多数据，不会立即return）并等待。此等待时间由max.block.ms=60000控制，表示的是：在等待多长时间后，若存在以下任一情况，则抛出异常：

- Producer 的send buffer沾满
- Broker不接收任何新数据
- 60s时间已过

如果出现这种类型的异常，则一般说明brokers 宕机，或是负载过高，导致无法响应请求。
