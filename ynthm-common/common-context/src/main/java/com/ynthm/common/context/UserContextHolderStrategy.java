package com.ynthm.common.context;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface UserContextHolderStrategy {
  void clearContext();

  UserContext getContext();

  void setContext(UserContext context);
}
