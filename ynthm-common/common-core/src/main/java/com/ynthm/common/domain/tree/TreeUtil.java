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
   * 构件树形结构的森林
   *
   * <p>内存中构建树 不包含顶层节点
   *
   * @param nodes 所有节点
   * @param root 顶层父ID
   * @return 结果森林
   * @param <T> 节点类型
   * @param <E> ID类型
   * @param <S> 排序类型
   */
  public static <T extends Node<E, S>, S extends Comparable<S>, E> List<T> buildTree(
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
                                    .sorted(Comparator.comparing(Node::sortable))
                                    .collect(Collectors.toList())))));
    // 循环设置对应的子节点
    nodes.forEach(
        node -> node.getChildren().addAll(id2Nodes.getOrDefault(node.getId(), new ArrayList<>())));
    // 返回第二层节点集合
    return nodes.stream()
        .filter(node -> node.getPid().equals(root))
        .sorted(Comparator.comparing(Node::sortable))
        .collect(Collectors.toList());
  }
}
