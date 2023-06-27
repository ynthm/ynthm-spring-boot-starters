package com.ynthm.common.util.id;

/**
 * 雪花算法
 *
 * @author Ethan Wang
 */
public class IdUtil {

  private static final IdGenerator idGenerator = new IdGenerator();

  private IdUtil() {

  }

  public static Long nextId() {
    return idGenerator.nextId();
  }
}
