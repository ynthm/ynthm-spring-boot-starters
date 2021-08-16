package com.ynthm.demo.mybatis.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ynthm.common.Result;
import com.ynthm.demo.mybatis.user.entity.User;
import com.ynthm.demo.mybatis.user.request.ChangePasswordRequest;
import com.ynthm.demo.mybatis.user.request.ForgetPasswordRequest;
import com.ynthm.demo.mybatis.user.request.RegisterRequest;
import com.ynthm.demo.mybatis.user.request.UserRequest;

/**
 * 用户服务类
 *
 * @author ynthm
 * @since 2020-05-09
 */
public interface UserService extends IService<User> {

  /**
   * 注册用户
   *
   * @param request
   * @return
   * @throws Exception
   */
  Result<String> register(RegisterRequest request) throws Exception;

  /**
   * 忘记用户密码
   *
   * @param request
   * @return
   */
  Result<Boolean> forgetPassword(ForgetPasswordRequest request);

  /**
   * 修改用户密码，登录用户
   *
   * @param request
   * @return
   */
  Result<Boolean> changePassword(ChangePasswordRequest request);

  /**
   * 账号是否存在
   *
   * @param request
   * @return
   */
  Boolean userExist(UserRequest request);

  User getUser(Long agentId);
}
