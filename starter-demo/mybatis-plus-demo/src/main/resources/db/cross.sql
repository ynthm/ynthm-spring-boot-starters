CREATE TABLE `cross_tenant`
(
    `id`           int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `tenant_id`    bigint unsigned NOT NULL COMMENT '租户ID',
    `tenant_name`  varchar(50) NOT NULL COMMENT '租户名称',
    `company_id`   varchar(64) NOT NULL COMMENT '公司主键id',
    `company_name` varchar(64) NOT NULL COMMENT '公司名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='cross: 租户表';



CREATE TABLE `cross_user`
(
    `id`               int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`          bigint      NOT NULL COMMENT '用户ID',
    `username`         varchar(30) NOT NULL COMMENT '用户名',
    `phone_number`     varchar(11) NOT NULL DEFAULT 'NULL' COMMENT '手机号码',

    `old_user_id`      varchar(64) NOT NULL COMMENT '老平台用户ID',
    `old_username`     varchar(64) NOT NULL COMMENT '老平台用户名',
    `old_phone_number` varchar(64) NOT NULL DEFAULT '-1' COMMENT '老平台手机号码'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='cross: 用户表';



CREATE TABLE `cross_dept`
(
    `id`               int unsigned PRIMARY KEY NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `tenant_id`        bigint                                                                DEFAULT NULL COMMENT '租户id',
    `dept_id`          bigint                                                       NOT NULL COMMENT '部门id',
    `parent_dept_id`   bigint                                                                DEFAULT '0' COMMENT '父部门id',
    `dept_ancestors`   varchar(512)                                                          DEFAULT '' COMMENT '祖级列表',
    `dept_name`        varchar(30)                                                           DEFAULT 'NULL' COMMENT '部门名称',

    `company_id`       varchar(64)                                                  NOT NULL COMMENT '所属公司id',
    `office_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织id',
    `parent_office_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父组织id',
    `office_id_path`   varchar(512)                                                 NOT NULL COMMENT '父组织id列表',
    `office_name`      varchar(64)                                                  NOT NULL DEFAULT 'NULL' COMMENT '组织名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='cross: 部门表';

-- 老平台公司数据
SELECT DISTINCT company_id,  company_name  FROM sys_company WHERE del_flag = 0

-- 老平台用户数据 包含公司
SELECT DISTINCT u.user_id, u.user_name, u.user_tel, s.company_id
FROM  sys_user u
          LEFT JOIN sys_staff s ON u.user_tel = s.company_tel
WHERE  u.del_flag = 0 AND s.del_flag = 0

-- 老平台部门数据
SELECT DISTINCT office_id,  parent_id, parent_ids, office_name, company_id FROM sys_office
WHERE del_flag = 0
ORDER BY parent_ids

-- 老平台 公司部门用户关系  sys_user_dept
SELECT DISTINCT so.office_id, u.user_id, s.company_id FROM sys_staff_office so
LEFT JOIN sys_staff s ON s.staff_id = so.staff_id
LEFT JOIN  sys_user u ON u.user_tel = s.company_tel
WHERE  u.del_flag = 0 AND s.del_flag = 0
  AND so.office_id <> ''

-- 老平台公司管理员
SELECT DISTINCT s.user_id, mg.company_id
FROM manage_group_staff gs
         LEFT JOIN sys_manage_group mg ON gs.group_id = mg.group_id
         LEFT JOIN sys_staff s ON s.staff_id = gs.staff_id
WHERE gs.del_flag = 0
  AND mg.del_flag = 0
  AND s.del_flag = 0