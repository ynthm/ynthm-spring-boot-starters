package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@SuperBuilder
@Data
public class BucketParam {
  @NotBlank
  protected String bucket;
  protected String region;
}
