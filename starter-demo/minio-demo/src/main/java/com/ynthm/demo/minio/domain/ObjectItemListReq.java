package com.ynthm.demo.minio.domain;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class ObjectItemListReq {
  @NotEmpty private List<ObjectItem> list;
}
