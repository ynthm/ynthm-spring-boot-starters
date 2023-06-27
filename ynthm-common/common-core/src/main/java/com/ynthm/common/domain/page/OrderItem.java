package com.ynthm.common.domain.page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 */
public class OrderItem implements Serializable {
  private static final long serialVersionUID = 1L;
  private String column;
  private boolean asc = true;

  public static OrderItem asc(String column) {
    return build(column, true);
  }

  public static OrderItem desc(String column) {
    return build(column, false);
  }

  public static List<OrderItem> ascs(String... columns) {
    return Arrays.stream(columns).map(OrderItem::asc).collect(Collectors.toList());
  }

  public static List<OrderItem> descs(String... columns) {
    return Arrays.stream(columns).map(OrderItem::desc).collect(Collectors.toList());
  }

  private static OrderItem build(String column, boolean asc) {
    return new OrderItem(column, asc);
  }

  public String getColumn() {
    return this.column;
  }

  public boolean isAsc() {
    return this.asc;
  }

  public void setColumn(final String column) {
    this.column = column;
  }

  public void setAsc(final boolean asc) {
    this.asc = asc;
  }

  protected boolean canEqual(final Object other) {
    return other instanceof OrderItem;
  }

  public OrderItem() {}

  public OrderItem(final String column, final boolean asc) {
    this.column = column;
    this.asc = asc;
  }
}
