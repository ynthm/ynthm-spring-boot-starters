package com.ynthm.demo.mybatis.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ynthm.common.Result;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.demo.mybatis.enums.BusinessCode;
import com.ynthm.demo.mybatis.user.entity.User;
import com.ynthm.demo.mybatis.user.mapper.UserMapper;
import com.ynthm.demo.mybatis.user.request.ChangePasswordRequest;
import com.ynthm.demo.mybatis.user.request.ForgetPasswordRequest;
import com.ynthm.demo.mybatis.user.request.RegisterRequest;
import com.ynthm.demo.mybatis.user.request.UserRequest;
import com.ynthm.demo.mybatis.user.service.UserRoleService;
import com.ynthm.demo.mybatis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * 服务实现类
 *
 * @author ynthm
 * @since 2020-05-09
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Autowired private HttpServletRequest httpServletRequest;

  private UserRoleService userRoleService;

  @Autowired
  public void setUserRoleService(UserRoleService userRoleService) {
    this.userRoleService = userRoleService;
  }

  @Override
  @Transactional(rollbackFor = {SQLException.class})
  public Result<String> register(RegisterRequest request) throws Exception {
    log.debug("请求参数 {}", request);

    String username = null;
    String agentCode = null;
    // 判断数据库账号是否存在
    // User tempUser;
    if (StringUtils.isNotBlank(request.getEmail())) {
      username = request.getEmail();
      agentCode = username;
      int userCount = count(Wrappers.<User>lambdaQuery().eq(User::getEmail, request.getEmail()));
      if (userCount > 0) {
        return Result.error(ResultCode.USERNAME_EXIST);
      }
    } else if (StringUtils.isNotBlank(request.getPhoneNumber())) {
      int userCount =
          count(
              Wrappers.<User>lambdaQuery()
                  .eq(User::getPhoneNumber, request.getPhoneNumber())
                  .eq(User::getAreaCode, request.getAreaCode()));
      if (userCount > 0) {
        return Result.error(ResultCode.USERNAME_EXIST);
      }

      username = request.getAreaCode() + request.getPhoneNumber();
      agentCode = request.getPhoneNumber();
    }

    User user = new User();
    BeanUtils.copyProperties(request, user);
    user.setUsername(username);
    user.setCompanyId(1L);
    user.setCreateTime(LocalDateTime.now());
    user.setEnabled(true);
    String uuid = UUID.randomUUID().toString();
    user.setUuid(uuid);
    boolean save = save(user);

    log.debug("账号注册成功 {}", user);
    return Result.ok("OK");
  }

  @Override
  public Result<Boolean> forgetPassword(ForgetPasswordRequest request) {
    // 判断数据库账号是否存在
    User tempUser =
        getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getUsername()));
    if (Objects.isNull(tempUser)) {
      return Result.error(ResultCode.USERNAME_NOT_EXIST);
    }

    update(
        Wrappers.<User>lambdaUpdate()
            .set(User::getPassword, request.getNewPassword())
            .eq(User::getId, tempUser.getId()));

    return Result.ok();
  }

  @Override
  public Result<Boolean> changePassword(ChangePasswordRequest request) {
    if (request.getPassword().equals(request.getNewPassword())) {
      return Result.error(BusinessCode.OLD_NEW_PASSWORD_SAME);
    }
    // 判断数据库账号是否存在
    User tempUser =
        getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getUsername()));
    ResultCode.USERNAME_NOT_EXIST.assertNotNull(tempUser);

    //    if (passwordEncoder.matches(request.getPassword(), tempUser.getPassword())) {
    //      update(
    //          Wrappers.<User>lambdaUpdate()
    //              .set(User::getPassword, passwordEncoder.encode(request.getNewPassword()))
    //              .eq(User::getId, tempUser.getId()));
    //    } else {
    //      return Result.error(BusinessCode.WRONG_PASSWORD);
    //    }

    return Result.ok();
  }

  @Override
  public Boolean userExist(UserRequest request) {

    if (StringUtils.isNotBlank(request.getEmail())) {
      int userCount = count(Wrappers.<User>lambdaQuery().eq(User::getEmail, request.getEmail()));
      if (userCount > 0) {
        return true;
      }
    } else if (StringUtils.isNotBlank(request.getPhoneNumber())) {
      int userCount =
          count(
              Wrappers.<User>lambdaQuery()
                  .eq(User::getPhoneNumber, request.getPhoneNumber())
                  .eq(User::getAreaCode, request.getAreaCode()));
      if (userCount > 0) {
        return true;
      }
    }

    return false;
  }

  @Override
  public User getUser(Long agentId) {
    return getOne(Wrappers.<User>lambdaQuery().eq(User::getAgentId, agentId), false);
  }

  public String generatePhoneNumber() {
    return "100" + String.format("%08d", baseMapper.getLastId());
  }
}
