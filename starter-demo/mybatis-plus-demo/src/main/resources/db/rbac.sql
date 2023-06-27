CREATE TABLE `user`
(
    `id`                 int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `username`           varchar(64) NOT NULL COMMENT '用户名',
    `phoneNumber`        varchar(16) NOT NULL COMMENT '手机号',
    `password`           varchar(64) NOT NULL COMMENT '密码',
    `gender`             tinyint     NOT NULL DEFAULT '-1' COMMENT '性别',
    `status`             tinyint     NOT NULL DEFAULT '0' COMMENT '状态',
    `created_by`         varchar(64) NOT NULL COMMENT '创建人',
    `created_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_modified_by`   varchar(64)          DEFAULT NULL COMMENT '最后修改人',
    `last_modified_time` datetime             DEFAULT NULL COMMENT '最后修改时间',
    `deleted`            tinyint(1) DEFAULT 0 COMMENT '逻辑删除状态',
    `version`            int unsigned DEFAULT 0 COMMENT '乐观锁版本'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户';

CREATE TABLE `role`
(
    `id`    int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`   int unsigned NOT NULL COMMENT '父ID',
    `level` int         NOT NULL COMMENT '层级',
    `name`  varchar(64) NOT NULL COMMENT '名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色';

CREATE TABLE `permission`
(
    `id`        int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`       int unsigned NOT NULL COMMENT '父ID',
    `type`      int          NOT NULL COMMENT '类型',
    `name`      varchar(64)  NOT NULL COMMENT '名称',
    `mark`      varchar(256) NOT NULL COMMENT '权限标识 eg 菜单为路径',
    `authority` varchar(256) NOT NULL COMMENT '权限 eg user:page, user:delete',
    `sort_no`   varchar(64)  NOT NULL COMMENT '名称',
    `enabled`   tinyint(1) DEFAULT 1 COMMENT '是否启用',
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='权限';

CREATE TABLE `role_permission`
(
    `id`            int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`       int unsigned NOT NULL COMMENT '角色ID',
    `permission_id` int unsigned NOT NULL COMMENT '权限ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色权限';

CREATE TABLE `organization`
(
    `id`   int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`  int unsigned NOT NULL COMMENT '父ID',
    `name` varchar(64) NOT NULL COMMENT '名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织';

CREATE TABLE `position`
(
    `id`   int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(64) NOT NULL COMMENT '名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='职位';

CREATE TABLE `group`
(
    `id`   int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name` varchar(64) NOT NULL COMMENT '名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分组';

CREATE TABLE `user_org`
(
    `id`      int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` int unsigned NOT NULL COMMENT '用户ID',
    `org_id`  int unsigned NOT NULL COMMENT '组织ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户组织';

CREATE TABLE `user_position`
(
    `id`          int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     int unsigned NOT NULL COMMENT '用户ID',
    `position_id` int unsigned NOT NULL COMMENT '职位ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户岗位';

CREATE TABLE `user_group`
(
    `id`       int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`  int unsigned NOT NULL COMMENT '用户ID',
    `group_id` int unsigned NOT NULL COMMENT '分组ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户分组';

CREATE TABLE `org_role`
(
    `id`      int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `org_id`  int unsigned NOT NULL COMMENT '组织ID',
    `role_id` int unsigned NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织角色';

CREATE TABLE `position_role`
(
    `id`          int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `position_id` int unsigned NOT NULL COMMENT '岗位ID',
    `role_id`     int unsigned NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位角色';

CREATE TABLE `group_role`
(
    `id`       int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `group_id` int unsigned NOT NULL COMMENT '分组ID',
    `role_id`  int unsigned NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='分组角色';

CREATE TABLE `user_role`
(
    `id`      int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` int unsigned NOT NULL COMMENT '用户ID',
    `role_id` int unsigned NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色';