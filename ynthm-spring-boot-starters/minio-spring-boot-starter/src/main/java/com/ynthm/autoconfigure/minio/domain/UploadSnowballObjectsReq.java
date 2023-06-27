package com.ynthm.autoconfigure.minio.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadSnowballObjectsReq extends BucketParam{
  @NotEmpty
  private List<InputStreamObject> objects;
}
