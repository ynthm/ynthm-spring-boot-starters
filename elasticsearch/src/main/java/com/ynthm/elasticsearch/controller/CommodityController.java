package com.ynthm.elasticsearch.controller;

import com.ynthm.elasticsearch.entity.Commodity;
import com.ynthm.elasticsearch.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommodityController {

  @Autowired public CommodityService commodityService;

  @RequestMapping("/commodity")
  public List<Commodity> get(Long id) {
    return commodityService.getAll();
  }
}
