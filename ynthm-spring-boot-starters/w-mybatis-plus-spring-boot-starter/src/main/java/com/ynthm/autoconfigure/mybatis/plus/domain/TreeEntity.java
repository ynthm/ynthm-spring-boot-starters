package com.ynthm.autoconfigure.mybatis.plus.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Tree基类
 *
 * @author Ethan Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeEntity extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /** 名称 */
  private String name;

  /** 父节点ID */
  private Long parentId;

  /** 显示顺序 */
  private Integer orderNum;

  /** 祖级列表 */
  private String ancestors;

  /** 子节点 */
  private List<? extends TreeEntity> children = new ArrayList<>();
}
