package com.ynthm.demo.mybatis.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ynthm.demo.mybatis.user.entity.User;

/**
 * Mapper 接口
 *
 * @author ynthm
 * @since 2020-05-09
 */
public interface UserMapper extends BaseMapper<User> {
  Long getLastId();
}