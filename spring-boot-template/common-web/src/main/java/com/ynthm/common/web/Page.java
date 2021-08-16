package com.ynthm.common.web;

import lombok.Data;

import java.util.List;

/** @author Ethan Wang */
@Data
public class Page<T> {
  private static final long serialVersionUID = 1L;
  private List<T> records;
  private long total;
  private long size;
  private long current;
}
