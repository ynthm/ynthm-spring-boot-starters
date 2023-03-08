package com.ynthm.demo.minio.domain;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class ObjectItemListReq {
  @NotEmpty
  private List<ObjectItem> list;
}
