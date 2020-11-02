package com.ynthm.elasticsearch.service;

import com.ynthm.elasticsearch.entity.Commodity;
import com.ynthm.elasticsearch.repository.CommodityRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityService {

  @Autowired private CommodityRepository commodityRepository;

  public long count() {
    return commodityRepository.count();
  }

  public Commodity save(Commodity commodity) {
    return commodityRepository.save(commodity);
  }

  public void delete(Commodity commodity) {
    commodityRepository.delete(commodity);
    //   commodityRepository.deleteById(commodity.getSkuId());
  }

  public List<Commodity> getAll() {
    return (List<Commodity>) commodityRepository.findAll();
  }

  public List<Commodity> getByName(String name) {
    List<Commodity> list = new ArrayList<>();
    MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", name);
    Iterable<Commodity> iterable = commodityRepository.search(matchQueryBuilder);
    iterable.forEach(list::add);
    return list;
  }

  public Page<Commodity> pageQuery(Integer pageNo, Integer pageSize, String kw) {
    return commodityRepository.findAll(PageRequest.of(pageNo, pageSize));
  }
}
