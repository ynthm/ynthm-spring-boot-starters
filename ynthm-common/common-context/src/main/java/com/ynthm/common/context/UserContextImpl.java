package com.ynthm.common.context;


/**
 * @author Ethan Wang
 * @version 1.0
 */
public class UserContextImpl implements UserContext {

  private AuthUser authUser;

  public UserContextImpl() {}

  public UserContextImpl(AuthUser authUser) {
    this.authUser = authUser;
  }

  @Override
  public AuthUser getAuthUser() {
    return authUser;
  }

  @Override
  public void setAuthUser(AuthUser authUser) {
    this.authUser = authUser;
  }
}
