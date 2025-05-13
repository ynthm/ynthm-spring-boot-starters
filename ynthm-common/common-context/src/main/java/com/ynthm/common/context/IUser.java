package com.ynthm.common.context;


/**
 * @author Ethan Wang
 */
public interface IUser {
  Long tenantId();
  
  Long userId();
  
  String username();
}