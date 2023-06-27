package com.ynthm.common.util.task;

/**
 * @author ethan
 */
public abstract class BaseTask implements Runnable {
  protected String id;

  public String getId() {
    return this.id;
  }
}
