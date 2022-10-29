package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ethan Wang
 */
@Data
public class BaseObject {
  @NotBlank private String bucket;
  /** object name or path/to/ */
  @NotBlank private String object;
}
