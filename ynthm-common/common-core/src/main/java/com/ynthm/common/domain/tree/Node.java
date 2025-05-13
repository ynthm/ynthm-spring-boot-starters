package com.ynthm.common.domain.tree;

import java.util.List;

/**
 * @author Ethan Wang
 */
public interface Node<E, S extends Comparable<S>> {

  E getId();

  E getPid();

  String getName();

  S sortable();

  List<Node<E, S>> getChildren();
}
