package com.ynthm.common.context;

import java.io.Serializable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface AuthUser extends Serializable {
  /**
   * 当前用户信息 用户、当前语言
   *
   * @return
   */
  Object getPrincipal();

  /**
   * Returns the name of this principal.
   *
   * @return the name of this principal.
   */
  String getName();
}
