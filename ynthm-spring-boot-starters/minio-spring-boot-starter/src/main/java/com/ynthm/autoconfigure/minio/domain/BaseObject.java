package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ethan Wang
 */
@Data
public class BaseObject extends BucketParam {
  /** object name or path/to/ */
  @NotBlank protected String object;
}
