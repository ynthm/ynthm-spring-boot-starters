package com.ynthm.demo.mybatis.user.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/** @author ethan */
@Data
public class ForgetPasswordRequest extends UserRequest {
  private static final long serialVersionUID = 8355802959021760447L;

  @NotEmpty(message = "{user.nb.password}")
  @Length(min = 6, max = 128, message = "密码最短6位到128位")
  private String newPassword;

  @NotBlank(message = "{user.nb.verification.code}")
  private String verificationCode;
}
