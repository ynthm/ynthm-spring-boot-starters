package com.ynthm.demo.mybatis.user.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/** @author ethan */
@Data
public class ChangePasswordRequest extends LoginRequest {
  private static final long serialVersionUID = -5449082109866936709L;

  @NotEmpty(message = "{user.nb.password}")
  @Length(min = 6, max = 128, message = "密码最短6位到128位")
  private String newPassword;
}
