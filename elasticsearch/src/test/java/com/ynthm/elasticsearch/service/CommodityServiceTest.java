package com.ynthm.elasticsearch.service;

import com.ynthm.elasticsearch.entity.Commodity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommodityServiceTest {

    @Autowired
    private CommodityService commodityService;

    @Test
    void count() {
        commodityService.count();
    }

    @Test
    void save() {

        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009001");
        commodity.setName("原味切片面包（10片装）");
        commodity.setCategory("101");
        commodity.setPrice(880);
        commodity.setBrand("良品铺子");
        commodityService.save(commodity);

        commodity = new Commodity();
        commodity.setSkuId("1501009002");
        commodity.setName("原味切片面包（6片装）");
        commodity.setCategory("101");
        commodity.setPrice(680);
        commodity.setBrand("良品铺子");
        commodityService.save(commodity);

        commodity = new Commodity();
        commodity.setSkuId("1501009004");
        commodity.setName("元气吐司850g");
        commodity.setCategory("101");
        commodity.setPrice(120);
        commodity.setBrand("百草味");
        commodityService.save(commodity);
    }

    @Test
    void delete() {
        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009002");
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