package com.ynthm.demo.mybatis.plus.user.service.impl;

import com.ynthm.demo.mybatis.plus.user.entity.User;
import com.ynthm.demo.mybatis.plus.user.mapper.UserMapper;
import com.ynthm.demo.mybatis.plus.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Ethan Wang
 * @since 2022-11-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
