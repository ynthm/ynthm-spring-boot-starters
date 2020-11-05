package com.ynthm.elasticsearch.fixture;

import com.ynthm.elasticsearch.entity.Commodity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ethan Wang
 * @version 1.0
 * @date 2020/11/3 11:40 上午
 */
public class CommodityFixture {
  public static Supplier<Commodity> ONE_SUPPLIER =
      () ->
          new Commodity()
              .setName("小米 10")
              .setBrand("小米")
              .setCategory("手机")
              .setPrice(1999.0)
              .setStock(1)
              .setTags(Arrays.asList("手机", "小米手机"));

  public static Supplier<List<Commodity>> DEFAULT_SUPPLIER =
      () ->
          Arrays.asList(
              new Commodity()
                  .setName("小米 10")
                  .setBrand("小米")
                  .setCategory("手机")
                  .setPrice(1999.0)
                  .setStock(1)
                  .setTags(Arrays.asList("手机", "小米手机")),
              new Commodity()
                  .setName("iPad Air 4")
                  .setBrand("苹果")
                  .setCategory("平板")
                  .setPrice(5999.0)
                  .setStock(10)
                  .setTags(Arrays.asList("平板", "苹果平板")),
              new Commodity()
                  .setName("iPhone 12")
                  .setBrand("Apple")
                  .setCategory("手机")
                  .setPrice(6999.0)
                  .setStock(10)
                  .setTags(Arrays.asList("手机", "苹果手机")));
}
