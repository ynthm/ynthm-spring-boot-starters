package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Data
public class RemoveObjectsReq extends BucketParam {
  @NotEmpty private List<String> objects;
}
