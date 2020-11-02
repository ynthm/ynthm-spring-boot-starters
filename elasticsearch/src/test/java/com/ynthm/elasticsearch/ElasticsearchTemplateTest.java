package com.ynthm.elasticsearch;

import com.ynthm.elasticsearch.entity.Commodity;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ElasticsearchTemplateTest {
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
    Commodity commodity = new Commodity();
    // commodity.setSkuId("1501009005");
    commodity.setName("葡萄吐司面包（10片装）");
    commodity.setCategory("101");
    commodity.setPrice(160);
    commodity.setBrand("良品铺子");
    Commodity save = elasticsearchRestTemplate.save(commodity);
    assertThat(save.getSkuId()).isNotBlank();
  }

  @Test
  void testQuery() {

    // 利用构造器建造NativeSearchQuery  他可以添加条件,过滤,等复杂操作
    NativeSearchQuery query =
        new NativeSearchQueryBuilder()
            //   .withQuery(QueryBuilders.matchQuery("title", "OPPOFindX"))
            .build();
    // elasticsearchRestTemplate.search方法参数一,本机查询的构造,参数二index的类,可选参数三再次声明库名(可以多个)
    SearchHits<Commodity> search = elasticsearchRestTemplate.search(query, Commodity.class);
    search.forEach(searchHit -> System.out.println(searchHit.getContent()));
  }

  @Test
  void testNativeSearchQueryBuilder2() {
    NativeSearchQuery query =
        new NativeSearchQueryBuilder()
            .withQuery(QueryBuilders.matchQuery("category", "手机"))
            // 添加分页  注意页码是从0开始的
            // pageable的实现类PageRequest的静态方法of
            // 要排序就增加参数3 Sort.Direction.ASC升  Sort.Direction.DESC降
            .withPageable(PageRequest.of(1, 4))
            // 排序整体
            // 根据字段排序fieldSort("字段名")   .order(SortOrder.ASC/DESC)
            .withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC))
            .build();
    // elasticsearchRestTemplate.search方法参数一,本机查询的构造,参数二index的类,可选参数三再次声明库名(可以多个)
    SearchHits<Commodity> search = elasticsearchRestTemplate.search(query, Commodity.class);
    search.forEach(searchHit -> System.out.println(searchHit.getContent()));
  }
}
