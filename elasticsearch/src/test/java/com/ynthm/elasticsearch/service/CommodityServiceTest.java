package com.ynthm.elasticsearch.service;

import com.ynthm.elasticsearch.entity.Commodity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

@SpringBootTest
class CommodityServiceTest {

  @Autowired private CommodityService commodityService;

  @Test
  void count() {
    commodityService.count();
  }

  @Test
  void save() {

    Commodity commodity = new Commodity();
    commodity.setId("111");
    commodity.setName("原味切片面包（10片装）");
    commodity.setCategory("101");
    commodity.setPrice(880.0);
    commodity.setBrand("良品铺子");
    commodityService.save(commodity);

    commodity = new Commodity();

    commodity.setName("原味切片面包（6片装）");
    commodity.setCategory("101");
    commodity.setPrice(680.0);
    commodity.setBrand("良品铺子");
    commodityService.save(commodity);

    commodity = new Commodity();

    commodity.setName("元气吐司850g");
    commodity.setCategory("101");
    commodity.setPrice(120.0);
    commodity.setBrand("百草味");
    commodityService.save(commodity);
  }

  @Test
  void delete() {
    Commodity commodity = new Commodity();
    commodity.setId("111");
    commodityService.delete(commodity);
  }

  @Test
  void getAll() {
    Iterable<Commodity> iterable = commodityService.getAll();
    iterable.forEach(e -> System.out.println(e.toString()));
  }

  @Test
  public void testGetByName() {
    List<Commodity> list = commodityService.getByName("面包");
    System.out.println(list);
  }

  @Test
  public void testPage() {
    Page<Commodity> page = commodityService.pageQuery(0, 10, "切片");
    System.out.println(page.getTotalPages());
    System.out.println(page.getNumber());
    System.out.println(page.getContent());
  }
}
