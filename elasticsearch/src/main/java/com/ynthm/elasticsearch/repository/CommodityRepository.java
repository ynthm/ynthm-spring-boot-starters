package com.ynthm.elasticsearch.repository;

import com.ynthm.elasticsearch.entity.Commodity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

/** @author Ethan Wang */
@Repository
public interface CommodityRepository extends ElasticsearchRepository<Commodity, String> {

  List<Commodity> findByNameContainsOrderByName(String name);

  /**
   * 价格区间查询
   *
   * @param left
   * @param right
   * @return
   */
  Stream<Commodity> findByPriceBetween(Integer left, Integer right);
}
