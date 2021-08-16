package com.ynthm.demo.mybatis.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.io.Serializable;

/** @author ynthm */
@Data
public class UserRequest implements Serializable {
  private static final long serialVersionUID = 1;

  @JsonIgnore private String username;
  @JsonIgnore private Boolean phone = false;

  @Email private String email;

  @Min(value = 0)
  private String areaCode;

  @Min(value = 0)
  private String phoneNumber;
}
