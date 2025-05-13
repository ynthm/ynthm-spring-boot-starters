package com.ynthm.autoconfigure.mybatis.plus.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库实体父类 包含审计信息及逻辑删除版本
 *
 * @author Ethan Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BvdEntity<T extends Model<?>> extends BaseEntity<T> {

  /**
   * 逻辑删除状态
   */
  @TableField("deleted")
  @TableLogic
  protected Boolean deleted;

  /**
   * 乐观锁版本
   */
  @TableField("version")
  @Version
  private Integer version;
}
