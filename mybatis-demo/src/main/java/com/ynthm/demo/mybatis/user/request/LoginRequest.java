package com.ynthm.demo.mybatis.user.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/** @author ynthm */
@Data
public class LoginRequest extends UserRequest implements Serializable {
  private static final long serialVersionUID = 1;

  @NotEmpty(message = "{user.nb.password}")
  @Length(min = 6, max = 128, message = "密码最短8位到128位")
  protected String password;
}
