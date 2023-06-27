package com.ynthm.common.context;

import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface UserContext extends Serializable {

  AuthUser getAuthUser();

  void setAuthUser(AuthUser authUser);
}
