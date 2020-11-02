package com.ynthm.elasticsearch;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html */
@SpringBootTest
public class RestHighLevelClientTest {

  @Autowired private RestHighLevelClient highLevelClient;

  /** Cluster Get Settings API */
  @Test
  void testCluster() throws IOException {
    ClusterGetSettingsRequest request = new ClusterGetSettingsRequest();
    request.includeDefaults(true);
    request.local(true);
    request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
    request.masterNodeTimeout("1m");
    ClusterGetSettingsResponse response =
        highLevelClient.cluster().getSettings(request, RequestOptions.DEFAULT);
    Settings persistentSettings = response.getPersistentSettings();
    Settings transientSettings = response.getTransientSettings();
    Settings defaultSettings = response.getDefaultSettings();
    String settingValue = response.getSetting("cluster.routing.allocation.enable");

    // index settings
    GetSettingsRequest getSettingsRequest = new GetSettingsRequest();
    GetSettingsResponse settings =
        highLevelClient.indices().getSettings(getSettingsRequest, RequestOptions.DEFAULT);
    for (ObjectObjectCursor<String, Settings> indexToSetting : settings.getIndexToSettings()) {
      System.out.println(indexToSetting.key);
    }
  }

  /**
   * Document APIs
   *
   * @throws IOException
   */
  @Test
  void testDocument() throws IOException {
    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("user", "Ethan");
    jsonMap.put("postDate", new Date());
    jsonMap.put("message", "trying out Elasticsearch");
    IndexRequest indexRequest = new IndexRequest("article", "article").source(jsonMap);
    IndexResponse indexResponse = highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    String index = indexResponse.getIndex();
    String id = indexResponse.getId();
    if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
      System.out.println("created");

    } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
      System.out.println("updated");
    }
    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
    if (shardInfo.getTotal() != shardInfo.getSuccessful()) {}

    if (shardInfo.getFailed() > 0) {
      for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
        String reason = failure.reason();
      }
    }
  }

  @Test
  public void test() throws IOException {

    CreateIndexRequest request = new CreateIndexRequest("twitter_two"); // 创建索引
    // 创建的每个索引都可以有与之关联的特定设置。
    request.settings(
        Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
    // 创建索引时创建文档类型映射
    request.mapping(
        "{\n"
            + "  \"properties\": {\n"
            + "    \"message\": {\n"
            + "      \"type\": \"text\"\n"
            + "    }\n"
            + "  }\n"
            + "}",
        XContentType.JSON);

    // 为索引设置一个别名
    request.alias(new Alias("twitter_alias"));
    // 可选参数
    request.setTimeout(TimeValue.timeValueMinutes(2)); // 超时,等待所有节点被确认(使用TimeValue方式)
    // request.timeout("2m");//超时,等待所有节点被确认(使用字符串方式)

    request.setMasterTimeout(TimeValue.timeValueMinutes(1)); // 连接master节点的超时时间(使用TimeValue方式)
    // request.masterNodeTimeout("1m");//连接master节点的超时时间(使用字符串方式)

    request.waitForActiveShards(ActiveShardCount.from(2)); // 在创建索引API返回响应之前等待的活动分片副本的数量，以int形式表示。
    // request.waitForActiveShards(ActiveShardCount.DEFAULT);//在创建索引API返回响应之前等待的活动分片副本的数量，以ActiveShardCount形式表示。

    // 同步执行
    CreateIndexResponse createIndexResponse =
        highLevelClient.indices().create(request, RequestOptions.DEFAULT);
    // 异步执行
    // 异步执行创建索引请求需要将CreateIndexRequest实例和ActionListener实例传递给异步方法：
    // CreateIndexResponse的典型监听器如下所示：
    // 异步方法不会阻塞并立即返回。
    ActionListener<CreateIndexResponse> listener =
        new ActionListener<CreateIndexResponse>() {
          @Override
          public void onResponse(CreateIndexResponse createIndexResponse) {
            // 如果执行成功，则调用onResponse方法;
          }

          @Override
          public void onFailure(Exception e) {
            // 如果失败，则调用onFailure方法。
          }
        };
    highLevelClient
        .indices()
        .createAsync(
            request,
            RequestOptions.DEFAULT,
            listener); // 要执行的CreateIndexRequest和执行完成时要使用的ActionListener

    // 返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
    boolean acknowledged = createIndexResponse.isAcknowledged(); // 指示是否所有节点都已确认请求
    boolean shardsAcknowledged =
        createIndexResponse.isShardsAcknowledged(); // 指示是否在超时之前为索引中的每个分片启动了必需的分片副本数
  }

  @Test
  public void testQuery1() throws IOException {

    SearchRequest searchRequest = new SearchRequest("article");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
    searchRequest.source(searchSourceBuilder);

    SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = response.getHits(); // 获得hits数组
    TotalHits totalHits = hits.getTotalHits(); // 获取检索的文档总数（不是这次返回的数量）
    for (SearchHit hit : hits) {
      String index = hit.getIndex(); // 获取文档的index
      String type = hit.getType(); // 获取文档的type
      String id = hit.getId(); // 获取文档的id
      Map<String, Object> sourceMap = hit.getSourceAsMap(); // 获取文档内容，封装为map
      String sourceString = hit.getSourceAsString(); // 获取文档内容，转换为json字符串。
      System.out.println(sourceString);
    }
  }
}
