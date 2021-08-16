package com.ynthm.demo.mybatis.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ynthm
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sys_user", resultMap = "rolesResultMap")
public class User extends Model<User> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  private String name;

  /** 登录名 */
  private String username;

  @JsonIgnore private String password;

  @Email private String email;

  private String areaCode;

  private String phoneNumber;

  private Long companyId;

  private Long agentId;

  @TableField("agent_invitation_code")
  private String agentInvitationCode;

  private String uuid;

  private LocalDateTime lastLoginTime;

  private LocalDateTime lastResetPasswordTime;

  /** Mybatis isEnable 与 getEnable 冲突 */
  @Getter(AccessLevel.NONE)
  private Boolean enabled;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.UPDATE)
  private LocalDateTime updateTime;

  private Boolean deleted;

  @TableField(exist = false)
  private List<Role> roleList;

  /** 用户gts2id */
  private Long gts2CustomerId;

  public String getLikeAgentId() {
    if (agentId.equals(1L)) {
      return "1/%";
    }
    return "1/" + agentId + "/%";
  }

  @Override
  public Serializable pkVal() {
    return this.id;
  }
}
