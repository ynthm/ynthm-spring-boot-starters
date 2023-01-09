package com.ynthm.demo.mybatis.plus.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ynthm.autoconfigure.mybatis.plus.domain.BaseEntity;
import com.ynthm.demo.mybatis.plus.enums.Gender;
import com.ynthm.demo.mybatis.plus.user.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户表
 *
 * @author Ethan Wang
 * @since 2022-11-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity<User> {

  private static final long serialVersionUID = 1L;

  /** 主键 ID */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /** 用户名 */
  @TableField("username")
  private String username;

  /** 手机号 */
  @TableField("phoneNumber")
  private String phoneNumber;

  /** 密码 */
  @TableField("password")
  private String password;

  /** 性别 */
  @TableField("gender")
  private Gender gender;

  /** 状态 */
  @TableField("status")
  private UserStatus status;

  /** 逻辑删除状态 */
  @TableField("deleted")
  @TableLogic
  private Boolean deleted;

  /** 乐观锁版本 */
  @TableField("version")
  @Version
  private Integer version;

  @Override
  public Serializable pkVal() {
    return this.id;
  }
}
