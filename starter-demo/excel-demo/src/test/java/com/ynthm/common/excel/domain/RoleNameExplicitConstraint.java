package com.ynthm.common.excel.domain;

import com.ynthm.common.excel.converter.ExcelConstraintInterface;
import java.util.Map;

/**
 * @author Ethan Wang
 */
public class RoleNameExplicitConstraint implements ExcelConstraintInterface {
@Override public String[] source(Map<String,Object> params) {
  return new String[] {"管理员", "角色2", "角色3", "角色4", "角色5"};
  }}
