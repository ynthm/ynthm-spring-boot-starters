package com.ynthm.demo.mybatis.user.controller;

import com.ynthm.common.Result;
import com.ynthm.common.enums.ResultCode;
import com.ynthm.common.web.util.ServletUtil;
import com.ynthm.demo.mybatis.enums.CaptchaScopeEnum;
import com.ynthm.demo.mybatis.user.request.*;
import com.ynthm.demo.mybatis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 前端控制器
 *
 * @author ynthm
 * @since 2020-05-09
 */
@Slf4j
@RestController
public class UserController {

  @Autowired private UserService userService;

  @PostMapping("/auth/code")
  public Result<Boolean> sendVerificationCode(@RequestBody @Valid VerificationCodeRequest request)
      throws IOException {
    // 判断账号是否已经注册
    if (CaptchaScopeEnum.REGISTER == request.getCaptchaScopeEnum()) {
      if (userService.userExist(request)) {
        return Result.error(ResultCode.USERNAME_EXIST);
      }
    }

    return Result.ok(true);
  }

  /**
   * 没有对象服务器图片简单存数据库
   *
   * @param files
   */
  @PostMapping(value = "/auth/register/files")
  public Result<List<Long>> upload(
      @RequestParam(value = "file", required = false) MultipartFile[] files) throws IOException {
    if (files != null) {}

    return Result.warn("没有文件上传。");
  }

  /**
   * 注册 校验验证码
   *
   * @param request
   */
  @PostMapping("/auth/register/verifyCode")
  public Result<String> registerVerify(@RequestBody @Valid VerifyVerificationCodeRequest request) {

    return Result.ok();
  }

  /**
   * 注册
   *
   * @param request
   */
  @PostMapping("/register")
  public Result<String> register(
      @RequestBody @Valid RegisterRequest request, HttpServletRequest httpServletRequest)
      throws Exception {
    request.setIp(ServletUtil.getIpAddr(httpServletRequest));
    request.setDomain(httpServletRequest.getServerName());
    // 匹配验证码

    return userService.register(request);
  }

  @PostMapping("/login")
  public void login(@RequestBody LoginRequest request) {
    throw new IllegalStateException(
        "This method shouldn't be called. It's implemented by Spring Security filters.");
  }

  /** 退出登录,这个接口在Spring Security 下没有效果，请使用命令或工具发POST请求 */
  @PostMapping("/logout")
  public void logout() {
    throw new IllegalStateException(
        "This method shouldn't be called. It's implemented by Spring Security filters.");
  }

  @PostMapping("/auth/forgetPassword")
  public Result<Boolean> changePasswordByVerificationCode(
      @Valid @RequestBody ForgetPasswordRequest request) {
    // 匹配验证码

    return userService.forgetPassword(request);
  }

  @PostMapping("/users/changePassword")
  public Result<Boolean> modifyPassword(@Valid @RequestBody ChangePasswordRequest request) {
    return userService.changePassword(request);
  }
}
