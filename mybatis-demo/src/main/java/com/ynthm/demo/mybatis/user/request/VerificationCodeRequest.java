package com.ynthm.demo.mybatis.user.request;

import com.ynthm.demo.mybatis.enums.CaptchaScopeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/** @author ethan */
@Data
public class VerificationCodeRequest extends UserRequest {

  private static final long serialVersionUID = 1L;

  /** eg: zh-CN */
  private String lang;

  /** 验证码范围(0，注册, 1、修改密码, 2 取币) */
  @NotNull private CaptchaScopeEnum captchaScopeEnum;
}
