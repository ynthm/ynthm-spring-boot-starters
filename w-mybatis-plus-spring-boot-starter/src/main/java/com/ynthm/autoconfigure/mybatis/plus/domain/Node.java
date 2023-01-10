package com.ynthm.autoconfigure.mybatis.plus.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 */
@Data
public class Node {
  private Integer id;
  private Integer pid;

  private String idPath;
  private String name;

  /** 显示顺序 */
  private Integer orderNum;

  private List<Node> children = new ArrayList<>();

  public Node() {
  }

  public Node(Integer id, Integer pid, String name) {
    this.id = id;
    this.pid = pid;
    this.name = name;
  }

  public Node copy() {
    return new Node(this.id, this.pid, this.name);
  }

  public void copy(Node source) {
    this.id = source.getId();
    this.pid = source.getPid();
  }
}
