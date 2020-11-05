package com.ynthm.elasticsearch;

import com.ynthm.elasticsearch.config.IndexNameConst;
import com.ynthm.elasticsearch.entity.Commodity;
import com.ynthm.elasticsearch.fixture.CommodityFixture;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ElasticsearchTemplateTest {
  @Autowired private ElasticsearchRestTemplate elasticsearchRestTemplate;

  @Test
  void test() {
    // 设置索引信息(绑定实体类)  返回IndexOperations
    IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Commodity.class);
    if (!indexOperations.exists()) { // 创建索引库
      indexOperations.create();
    }

    assertThat(indexOperations.exists()).isTrue();
    System.out.println(indexOperations.delete());
    assertThat(indexOperations.exists()).isFalse();
  }

  @Test
  void testInsert() {
    Iterable<Commodity> save =
        elasticsearchRestTemplate.save(CommodityFixture.DEFAULT_SUPPLIER.get());
    for (Commodity commodity : save) {
      assertThat(commodity.getId()).isNotBlank();
    }
  }

  @Test
  void testQueryAll() {

    // 利用构造器建造NativeSearchQuery  他可以添加条件,过滤,等复杂操作
    NativeSearchQuery query = new NativeSearchQueryBuilder().build();
    // elasticsearchRestTemplate.search方法参数一,本机查询的构造,参数二index的类,可选参数三再次声明库名(可以多个)
    SearchHits<Commodity> search = elasticsearchRestTemplate.search(query, Commodity.class);
    assertThat(search.getTotalHits()).isNotNegative();
    search.forEach(searchHit -> System.out.println(searchHit.getContent()));
  }

  @Test
  void testDeleteOne() {
    Commodity one = elasticsearchRestTemplate.save(CommodityFixture.ONE_SUPPLIER.get());
    String count =
        elasticsearchRestTemplate.delete(
            one.getId(), IndexCoordinates.of(IndexNameConst.COMMODITY));
    assertThat(count).isNotBlank();
  }

  @Test
  void testDelete2() {
    elasticsearchRestTemplate.delete(
        new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", "小米 1")).build(),
        Commodity.class,
        IndexCoordinates.of(IndexNameConst.COMMODITY));
    SearchHits<Commodity> search =
        elasticsearchRestTemplate.search(
            new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).build(),
            Commodity.class);
    search.forEach(System.out::println);
    assertThat(search.getTotalHits()).isNotNegative();
  }
  /** 排序分页 */
  @Test
  void testNativeSearchQueryBuilder2() {
    NativeSearchQuery query =
        new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.matchQuery("category", "手机"))
            // 添加分页  注意页码是从0开始的
            // pageable的实现类PageRequest的静态方法of
            // 要排序就增加参数3 Sort.Direction.ASC升  Sort.Direction.DESC降
            .withPageable(PageRequest.of(0, 3))
            // 排序整体
            // 根据字段排序fieldSort("字段名")   .order(SortOrder.ASC/DESC)
            .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))
            .build();
    // elasticsearchRestTemplate.search方法参数一,本机查询的构造,参数二index的类,可选参数三再次声明库名(可以多个)
    SearchHits<Commodity> search = elasticsearchRestTemplate.search(query, Commodity.class);
    search.forEach(searchHit -> System.out.println(searchHit.getContent()));
    assertThat(search.getTotalHits()).isNotNegative();
  }

  /** 聚合查询 */
  @Test
  void testAgg1() {
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    // 聚合可以有多个,所以add
    // terms词条聚合,传入聚合名称   field("聚合字段")   size(结果集大小)
    NativeSearchQuery query =
        nativeSearchQueryBuilder
            .addAggregation(AggregationBuilders.terms("brands").field("brand"))
            // 结果集过滤  这里设置不需要结果集(不添加包含与不包含,会自动生成length为0数组)
            .withSourceFilter(new FetchSourceFilterBuilder().build())
            .build();
    SearchHits<Commodity> hits = elasticsearchRestTemplate.search(query, Commodity.class);
    System.out.println(hits);
    // 获取聚合结果集   因为结果为字符串类型 所以用ParsedStringTerms接收   还有ParsedLongTerms接收数字  ParsedDoubleTerms接收小数
    Aggregations aggregations = hits.getAggregations();
    assert aggregations != null;
    ParsedStringTerms brands = aggregations.get("brands");
    // 获取桶
    brands
        .getBuckets()
        .forEach(
            bucket -> {
              // 获取桶中的key   与    记录数
              System.out.println(bucket.getKeyAsString() + " " + bucket.getDocCount());
            });
  }

  /** 嵌套聚合查询 */
  @Test
  void testAgg2() {
    NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    // 聚合可以有多个,所以add
    // terms词条聚合,传入聚合名称   field("聚合字段")
    NativeSearchQuery query =
        nativeSearchQueryBuilder
            .addAggregation(
                AggregationBuilders.terms("brands")
                    .field("brand")
                    // 添加子聚合  subAggregation(添加方式是一样的)  值为桶中品牌均价
                    .subAggregation(AggregationBuilders.avg("price_avg").field("price")))
            // 结果集过滤  这里设置不需要结果集(不添加包含与不包含,会自动生成长为0数组)
            .withSourceFilter(new FetchSourceFilterBuilder().build())
            .build();
    SearchHits<Commodity> hits = elasticsearchRestTemplate.search(query, Commodity.class);
    System.out.println(hits);
    // 获取聚合结果集   因为结果为字符串类型 所以用ParsedStringTerms接收   还有ParsedLongTerms接收数字  ParsedDoubleTerms接收小数
    Aggregations aggregations = hits.getAggregations();
    assert aggregations != null;
    ParsedStringTerms brands = aggregations.get("brands");
    // 获取桶brands
    brands
        .getBuckets()
        .forEach(
            bucket -> {
              // 获取桶中的key   与    记录数
              System.out.println(bucket.getKeyAsString() + " " + bucket.getDocCount());

              // 获取嵌套的桶price_avg
              ParsedAvg price_avg = bucket.getAggregations().get("price_avg");
              System.out.println(price_avg.getValue());
            });
  }
}
