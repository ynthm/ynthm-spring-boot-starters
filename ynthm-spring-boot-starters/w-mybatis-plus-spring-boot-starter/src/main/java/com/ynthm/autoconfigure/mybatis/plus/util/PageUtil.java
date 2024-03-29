package com.ynthm.autoconfigure.mybatis.plus.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ynthm.common.domain.page.OrderItem;
import com.ynthm.common.domain.page.PageReq;
import com.ynthm.common.domain.page.PageResp;
import com.ynthm.common.domain.page.PageValidReq;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 与第三方包分页实体转换
 *
 * @author Ethan Wang
 */
public class PageUtil {

  private PageUtil() {}

  /**
   * 框架分页请求转化 三方框架请求参数
   *
   * @return IPage
   */
  public static <T> IPage<T> pageable(PageReq<?> req) {
    return getPage(req.getPage(), req.getSize(), req.getOrderItems(), req.isSearchCount());
  }

  public static <T> IPage<T> pageable(PageValidReq<?> req) {
    return getPage(req.getPage(), req.getSize(), req.getOrderItems(), req.isSearchCount());
  }

  private static <T> IPage<T> getPage(
      int pageIndex, int size, List<OrderItem> orderItems2, boolean searchCount) {
    Page<T> page = new Page<>(pageIndex, size);
    page.setOrders(
        Optional.ofNullable(orderItems2)
            .map(
                orderItems ->
                    orderItems.stream()
                        .map(i -> new com.baomidou.mybatisplus.core.metadata.OrderItem())
                        .collect(Collectors.toList()))
            .orElse(new ArrayList<>()));
    page.setSearchCount(searchCount);
    return page;
  }

  public static <T> IPage<T> pageableMax() {
    return new Page<>(1, Integer.MAX_VALUE);
  }

  /**
   * 分页查询结果转化
   *
   * @param page Mybatis Plus 框架结果
   * @return 统一分页结果
   * @param <T> 参数类型
   */
  public static <T> PageResp<T> from(IPage<T> page) {
    return new PageResp<T>()
        .setTotal((int) page.getTotal())
        .setTotalPage((int) page.getPages())
        .setRecords(page.getRecords());
  }

  /**
   * 批量查询数据库场景
   *
   * @param req 分页请求
   * @param func Mybatis 处理分页请求
   * @param <T> 结果类型
   * @param <P> 参数
   */
  public static <T extends Serializable, P extends Serializable> PageResp<T> pageable(
      PageReq<P> req, BiFunction<IPage<T>, P, Page<T>> func) {
    return from(func.apply(pageable(req), req.getParam()));
  }

  public static <T, R> PageResp<R> from(IPage<T> page, Function<T, R> mapping) {
    return new PageResp<R>()
        .setTotal((int) page.getTotal())
        .setTotalPage((int) page.getPages())
        .setRecords(page.getRecords().stream().map(mapping).collect(Collectors.toList()));
  }
}
