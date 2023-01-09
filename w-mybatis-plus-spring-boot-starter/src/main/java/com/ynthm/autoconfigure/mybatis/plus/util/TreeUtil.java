package com.ynthm.autoconfigure.mybatis.plus.util;



import com.ynthm.autoconfigure.mybatis.plus.domain.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ethan Wang
 */
public class TreeUtil {

  /**
   * 采用先构件树，固定住每个节点的引用地址，创建虚拟的父节点，再补全。
   * <p> 数据库 with 递归查询树所有节点记录
   *
   * @param nodes
   * @return
   */
  public static List<Node> buildTree(List<Node> nodes) {
    Map<Integer, Node> nodeMap = new HashMap<>(nodes.size());
    List<Node> roots = new ArrayList<>();
    for (Node node : nodes) {
      int id = node.getId();
      Node n = nodeMap.get(id);
      if (n == null) {
        n = node;
        nodeMap.put(id, node);
      } else {
        int pid = n.getPid();
        if (pid == -1) {
          // 代表虚拟先存节点 替换数据
          n.copy(node);
        }
      }
      int pid = node.getPid();
      Node pn = nodeMap.get(pid);
      if (pn == null) {
        // 父节点不存在 先设置 -1
        pn = new Node(pid, -1, "");
        if (pid == 0) {
          roots.add(pn);
        }
        nodeMap.put(pid, pn);
      }
      pn.getChildren().add(n);
    }
    return roots;
  }


  public List<Node> buildTree1(List<Node> nodes) {
    //先选出非顶级的节点
    List<Node> list = nodes.stream().filter(node -> node.getPid() != 0).collect(Collectors.toList());

    //将这些非顶级节点的数据按pid进行分组
    Map<Integer, List<Node>> sub = list.stream().collect(Collectors.groupingBy(node -> node.getPid()));

    //循环设置对应的子节点（根据id = pid）
    nodes.forEach(node -> node.setChildren(sub.get(node.getId())));

    //过滤掉父节点数据
    List<Node> collect = nodes.stream().filter(node -> node.getPid() == 0).collect(Collectors.toList());
    return collect;
  }

}
