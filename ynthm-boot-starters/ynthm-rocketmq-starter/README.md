# 说明

消息结构

基本字段：

- messageId：消息唯一标识符。 
- topic：消息主题，用于指定消息类型。 
- tags：标签，用于对消息进行分类。 
- key：业务主键，便于快速检索。 
- body：消息内容，通常为 JSON 或其他格式。

```json
{
  "messageId": "12345",
  "topic": "order_topic",
  "tags": ["order", "payment"],
  "key": "order_67890",
  "body": {
    "orderId": "67890",
    "userId": "user123",
    "amount": 100.50,
    "status": "pending",
    "timestamp": "2024-08-08T12:00:00Z"
  }
}
```

```yaml
rocketmq:
  name-server: localhost:9876
```

## 更具单 tag 反序列化

tags 消息标签，方便服务器过滤使用。目前只支持每个消息设置一个标签

```java
    @Component
    @RocketMQMessageListener(topic = "demo-topic", consumerGroup = "demo-group")
    public static class DemoConsumer implements RocketMQListener<String> {
        public void onMessage(String message) {
            System.out.println("received message: " + message);
        }
    }
```