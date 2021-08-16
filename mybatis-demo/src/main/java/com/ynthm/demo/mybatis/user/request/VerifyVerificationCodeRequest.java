package com.ynthm.demo.mybatis.user.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class VerifyVerificationCodeRequest extends UserRequest {

  /** 验证码 */
  @NotEmpty(message = "{user.nb.verification.code}")
  private String verificationCode;
}
