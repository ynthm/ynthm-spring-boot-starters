package com.ynthm.starter.jpa.util;

import com.ynthm.common.domain.PageReq;
import com.ynthm.common.domain.PageResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class PageUtil {
  private PageUtil() {}

  public static <P extends Serializable> Pageable pageable(PageReq<P> req) {
    return PageRequest.of(
        req.getPage(),
        req.getSize(),
        Optional.ofNullable(req.getOrderItems())
            .map(
                orderItems ->
                    orderItems.stream()
                        .map(
                            orderItem -> {
                              if (orderItem.isAsc()) {
                                return Sort.Order.asc(orderItem.getColumn());
                              } else {
                                return Sort.Order.desc(orderItem.getColumn());
                              }
                            })
                        .collect(Collectors.toList()))
            .map(Sort::by)
            .orElseGet(Sort::unsorted));
  }

  public static <T extends Serializable> PageResp<T> from(Page<T> page) {
    return new PageResp<T>()
        .setTotal((int) page.getTotalElements())
        .setTotalPage(page.getTotalPages())
        .setRecords(page.getContent());
  }

  public static <T extends Serializable, P extends Serializable> PageResp<T> page(
      Function<Pageable, Page<T>> func, PageReq<P> req) {
    return from(func.apply(pageable(req)));
  }
}
