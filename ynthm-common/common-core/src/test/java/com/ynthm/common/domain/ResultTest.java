package com.ynthm.common.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.ynthm.common.enums.BaseResultCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class ResultTest {

  @Test
  void errorReturnAndLog() {

    Result<Void> result =
        Result.errorReturnAndLog(BaseResultCode.DB_NOT_EXIST, "表: {}; id: {}", "table_name", 1L);
    Assertions.assertEquals(BaseResultCode.DB_NOT_EXIST.getCode(), result.getCode());
  }
}
