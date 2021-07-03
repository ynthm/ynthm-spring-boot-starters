package com.ynthm.common.api;

import lombok.Data;

import java.util.List;

/** @author Ethan Wang */
@Data
public class Page<T> {
  private static final long serialVersionUID = 1L;
  private List<T> records;
  private long total;
  private int size;
  private int current;
}
