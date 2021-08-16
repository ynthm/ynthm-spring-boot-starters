package com.ynthm.demo.mybatis.user.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/** @author ethan */
@Data
@ToString
public class RegisterRequest extends UserRequest implements Serializable {

  private static final long serialVersionUID = 1;

  private String name;

  @NotEmpty(message = "{user.nb.password}")
  private String password;

  private String agentInvitationCode;

  @NotEmpty(message = "{user.nb.verification.code}")
  private String verificationCode;

  private String domain;

  private String ip;
}
