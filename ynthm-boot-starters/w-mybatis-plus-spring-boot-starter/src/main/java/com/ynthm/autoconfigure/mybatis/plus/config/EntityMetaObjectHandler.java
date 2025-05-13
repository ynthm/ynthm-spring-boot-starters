package com.ynthm.autoconfigure.mybatis.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ynthm.common.context.AuthUser;
import com.ynthm.common.context.IUser;
import com.ynthm.common.context.UserContext;
import com.ynthm.common.context.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 数据库实体填充
 *
 * @see com.ynthm.autoconfigure.mybatis.plus.domain.BaseEntity
 * @author Ethan Wang
 */
@Slf4j
public class EntityMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    log.debug("start insert fill ....");
    // 相关字段必须指定  @TableField(fill = FieldFill.INSERT)
    Optional.ofNullable(UserContextHolder.getContext())
        .map(UserContext::getAuthUser)
        .map(AuthUser::getName)
        .ifPresent(
            username ->
                this.strictInsertFill(metaObject, "createdBy", () -> username, String.class));

    Optional.ofNullable(UserContextHolder.getContext())
        .map(UserContext::getAuthUser)
        .ifPresent(
            authUser -> {
              Optional.ofNullable(authUser.getName())
                  .ifPresent(
                      username ->
                          this.strictInsertFill(
                              metaObject, "createdBy", () -> username, String.class));

              if (authUser.getPrincipal() instanceof IUser) {
                Long tenantId = ((IUser) authUser.getPrincipal()).tenantId();
                if (tenantId != null) {
                  this.setFieldValByName("tenantId", tenantId, metaObject);
                }
              }
            });

    this.strictInsertFill(
            metaObject, "createdTime", LocalDateTime::now, LocalDateTime.class);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    log.debug("start update fill ....");
    Optional.ofNullable(UserContextHolder.getContext())
        .map(UserContext::getAuthUser)
        .map(AuthUser::getName)
        .ifPresent(
            username ->
                this.strictInsertFill(metaObject, "lastModifiedBy", () -> username, String.class));
    this.strictUpdateFill(
            metaObject, "lastModifiedTime", LocalDateTime::now, LocalDateTime.class);
  }
}
