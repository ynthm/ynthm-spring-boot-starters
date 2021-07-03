package com.ynthm.spring.web.demo.excel.client;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ynthm.spring.web.demo.excel.excel.ExplicitConstraint;
import lombok.Data;

/** @author Ethan Wang */
@Data
public class User {
  @ExcelProperty("名字")
  private String name;

  @ExcelProperty("年龄")
  private Integer age;

  @ExplicitConstraint(source = {"男", "女"})
  @ExcelProperty("性别")
  private String sex;

  @ExplicitConstraint(sourceClass = RoleNameExplicitConstraint.class)
  private String roleName;
}
