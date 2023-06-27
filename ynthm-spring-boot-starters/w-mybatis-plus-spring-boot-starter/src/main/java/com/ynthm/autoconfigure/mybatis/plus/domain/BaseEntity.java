package com.ynthm.autoconfigure.mybatis.plus.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据库实体父类 包含审计信息
 *
 * @author Ethan Wang
 */
@Data
public abstract class BaseEntity<T extends Model<?>> extends Model<T> {

  /**
   * 创建人
   */
  @TableField(fill = FieldFill.INSERT)
  protected String createdBy;
  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  protected LocalDateTime createdTime;
  /**
   * 最后修改人
   */
  @TableField(fill = FieldFill.UPDATE)
  protected String lastModifiedBy;
  /**
   * 最后修改时间
   */
  @TableField(fill = FieldFill.UPDATE)
  protected LocalDateTime lastModifiedTime;
}
