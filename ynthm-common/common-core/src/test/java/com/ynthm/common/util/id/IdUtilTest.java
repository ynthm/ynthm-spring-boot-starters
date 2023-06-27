package com.ynthm.common.util.id;

import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class IdUtilTest {

  @Test
  void nextId() {
    for (int i = 0; i < 10; i++) {
      System.out.println(IdUtil.nextId());
    }
  }
}
