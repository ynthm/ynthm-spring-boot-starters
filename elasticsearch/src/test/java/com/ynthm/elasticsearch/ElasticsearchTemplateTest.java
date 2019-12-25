package com.ynthm.elasticsearch;

import com.ynthm.elasticsearch.entity.Commodity;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.*;

import java.util.List;

@SpringBootTest
public class ElasticsearchTemplateTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchOperations;

    @Test
    void test() {
        elasticsearchOperations.createIndex(Commodity.class);
        System.out.println(elasticsearchOperations.indexExists("commodity"));
        System.out.println(elasticsearchOperations.deleteIndex(Commodity.class));

    }

    @Test
    void testInsert() {
        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009005");
        commodity.setName("葡萄吐司面包（10片装）");
        commodity.setCategory("101");
        commodity.setPrice(160);
        commodity.setBrand("良品铺子");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(commodity).build();
        elasticsearchOperations.index(indexQuery);
    }

    @Test
    public void testQuery() {

        SearchQuery all = new NativeSearchQueryBuilder().build();

        List<Commodity> commodities = elasticsearchOperations.queryForList(all, Commodity.class);


        Commodity commodity = elasticsearchOperations.queryForObject(GetQuery.getById("1501009005"), Commodity.class);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "吐司"))
                .build();

        List<Commodity> list = elasticsearchOperations.queryForList(searchQuery, Commodity.class);
        System.out.println(list);
    }
}
