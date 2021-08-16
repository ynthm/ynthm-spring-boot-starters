CREATE TABLE "office"."sys_role" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar(256),
  "create_time" timestamptz(6)
)
;

ALTER TABLE "office"."sys_role"
  OWNER TO "agentoffice";

COMMENT ON COLUMN "office"."sys_role"."id" IS 'ID';
COMMENT ON COLUMN "office"."sys_role"."name" IS '角色名';
COMMENT ON COLUMN "office"."sys_role"."create_time" IS '创建时间';


CREATE TABLE "office"."sys_user" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(64),
  "username" varchar(128),
  "password" varchar(1024),
  "email" varchar(128),
  "area_code" varchar(16),
  "phone_number" varchar(32) ,
  "company_id" int8 NOT NULL DEFAULT 1,
  "gts2_customer_id" int8,
  "agent_id" int8,
  "agent_invitation_code" varchar(32),
  "uuid" varchar(64),
  "last_login_time" timestamptz(6),
  "last_reset_password_time" timestamptz(6),
  "enabled" bool DEFAULT true,
  "deleted" bool DEFAULT false,
  "create_time" timestamptz(6),
  "update_time" timestamptz(6)
)
;


ALTER TABLE "office"."sys_user"
  OWNER TO "agentoffice";

COMMENT ON COLUMN "office"."sys_user"."id" IS 'ID';
COMMENT ON COLUMN "office"."sys_user"."name" IS '用户名';
COMMENT ON COLUMN "office"."sys_user"."username" IS '账号';
COMMENT ON COLUMN "office"."sys_user"."password" IS '密码 ';
COMMENT ON COLUMN "office"."sys_user"."email" IS '邮箱 登录账号';
COMMENT ON COLUMN "office"."sys_user"."area_code" IS '区号+手机号 登录账号';
COMMENT ON COLUMN "office"."sys_user"."phone_number" IS '区号+手机号 登录账号';
COMMENT ON COLUMN "office"."sys_user"."company_id" IS '公司ID';
COMMENT ON COLUMN "office"."sys_user"."gts2_customer_id" IS '用户gts2id';
COMMENT ON COLUMN "office"."sys_user"."agent_id" IS '代理ID';
COMMENT ON COLUMN "office"."sys_user"."agent_invitation_code" IS '代理邀请码';
COMMENT ON COLUMN "office"."sys_user"."last_login_time" IS '最后登录时间';
COMMENT ON COLUMN "office"."sys_user"."last_reset_password_time" IS '最后修改密码时间';
COMMENT ON COLUMN "office"."sys_user"."enabled" IS '账号是否启用';
COMMENT ON COLUMN "office"."sys_user"."deleted" IS '是否删除 默认为否';
COMMENT ON COLUMN "office"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "office"."sys_user"."update_time" IS '更新时间';


CREATE TABLE "office"."sys_user_role" (
  "id" SERIAL PRIMARY KEY,
  "user_id" int8,
  "role_id" int4,
  "role_name" varchar(128),
  "create_time" timestamptz(6),
  "update_time" timestamptz(6)
)
;

ALTER TABLE "office"."sys_user_role"
  OWNER TO "agentoffice";

COMMENT ON COLUMN "office"."sys_user_role"."id" IS 'ID';
COMMENT ON COLUMN "office"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "office"."sys_user_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "office"."sys_user_role"."role_name" IS '角色名';
COMMENT ON COLUMN "office"."sys_user_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "office"."sys_user_role"."update_time" IS '更新时间';

INSERT INTO "office"."sys_role"("id", "name", "create_time") VALUES (1, 'ROLE_ADMIN', '2020-05-09 09:38:08+08');
INSERT INTO "office"."sys_role"("id", "name", "create_time") VALUES (2, 'ROLE_AGENT', '2020-05-09 09:38:23+08');
-- INSERT INTO "office"."sys_user"("id", "username", "password", "email", "area_code", "phone_number", "company_id", "agent_id", "agent_invitation_code", "last_login_time", "last_reset_password_time", "deleted", "create_time", "update_time", "enabled", "name") VALUES (1, 'iwys@qq.com', '$2a$10$clV0SQD3tD8chwKD9gswpO4DAXyTOaUFMTMvdBjtf4XiqQEgO/Z1i', 'iwys@qq.com', NULL, NULL, 1, 1, NULL, NULL, NULL, 'f', '2020-05-11 11:05:16.277+08', NULL, 't', 'Ethan');
-- INSERT INTO "office"."sys_user_role"("id", "user_id", "role_id", "role_name", "create_time", "update_time") VALUES (1, 1, 1, 'ROLE_ADMIN', '2020-05-11 20:11:47+08', NULL);