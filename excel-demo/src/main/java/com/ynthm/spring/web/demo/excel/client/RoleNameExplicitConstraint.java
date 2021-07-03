package com.ynthm.spring.web.demo.excel.client;

import com.ynthm.spring.web.demo.excel.excel.ExplicitInterface;

/** @author Ethan Wang */
public class RoleNameExplicitConstraint implements ExplicitInterface {
  @Override
  public String[] source() {
    return new String[] {"管理员", "角色2", "角色3", "角色4", "角色5"};
  }
}
