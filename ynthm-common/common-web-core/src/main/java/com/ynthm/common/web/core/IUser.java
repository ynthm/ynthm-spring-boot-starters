package com.ynthm.common.web.core;


/**
 * @author Ethan Wang
 */
public interface IUser {
  Long tenantId();
  
  Long userId();
  
  String username();
}