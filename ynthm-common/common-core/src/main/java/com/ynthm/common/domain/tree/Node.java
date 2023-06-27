package com.ynthm.common.domain.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Data;

/**
 * @author Ethan Wang
 */
@Data
public class Node<E extends Comparable<E>> implements Serializable {

  protected E id;
  protected E pid;
  protected String name;

  protected Integer sortNo;

  /** 节点类型 */
  protected Integer type;

  protected List<Node> children = new ArrayList<>();

  public Node() {}

  public Node(E id, E pid, String name) {
    this.id = id;
    this.pid = pid;
    this.name = name;
  }

  public void addChildren(Collection<? extends Node> c) {
    children.addAll(c);
  }
}
