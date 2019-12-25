package com.ynthm.elasticsearch.repository;

import com.ynthm.elasticsearch.entity.Commodity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends ElasticsearchRepository<Commodity, String> {
}
