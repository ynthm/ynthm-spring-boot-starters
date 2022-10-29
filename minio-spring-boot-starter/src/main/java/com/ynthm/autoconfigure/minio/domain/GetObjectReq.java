package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ethan Wang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetObjectReq extends BaseObject {
  /** -1 从开始读取 */
  private long offset = -1L;

  private long length = -1L;
}
