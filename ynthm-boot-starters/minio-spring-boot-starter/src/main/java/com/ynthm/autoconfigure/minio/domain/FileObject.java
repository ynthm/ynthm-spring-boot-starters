package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;

/**
 * @author Ethan Wang
 */
@Data
public class FileObject {
  private String objectName;
  private String file;
}
