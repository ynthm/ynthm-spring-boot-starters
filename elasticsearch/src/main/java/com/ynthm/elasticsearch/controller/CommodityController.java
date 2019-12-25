package com.ynthm.elasticsearch.controller;

import com.ynthm.elasticsearch.entity.Commodity;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommodityController {

    @Autowired
    public ElasticsearchOperations elasticsearchOperations;

    @RequestMapping("/commodity")
    public List<Commodity> get() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name", "吐司"))
                .build();
        return elasticsearchOperations.queryForList(searchQuery, Commodity.class);
    }
}
