package com.ynthm.common.domain.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 */
public class TreeUtil {
  private TreeUtil() {}

  /**
   * 内存中构建树 不包含顶层节点
   *
   * @param nodes
   * @param root 根节点值
   * @return
   * @param <T>
   * @param <E>
   */
  public static <T extends Node<E>, E extends Comparable<E>> List<T> buildTree(
      List<T> nodes, E root) {
    Map<E, List<T>> id2Nodes =
        nodes.stream()
            .filter(node -> !node.getPid().equals(root))
            .collect(
                Collectors.groupingBy(
                    Node::getPid,
                    Collectors.mapping(
                        Function.identity(),
                        Collectors.collectingAndThen(
                            Collectors.toList(),
                            e ->
                                e.stream()
                                    .sorted(Comparator.comparing(Node::getSortNo))
                                    .collect(Collectors.toList())))));
    // 循环设置对应的子节点
    nodes.forEach(node -> node.addChildren(id2Nodes.getOrDefault(node.getId(), new ArrayList<>())));
    // 返回第二层节点集合
    return nodes.stream()
        .filter(node -> node.getPid().equals(root))
        .sorted(Comparator.comparing(Node::getSortNo))
        .collect(Collectors.toList());
  }
}
