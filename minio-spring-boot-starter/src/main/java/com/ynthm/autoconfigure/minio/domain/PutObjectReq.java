package com.ynthm.autoconfigure.minio.domain;

import java.io.InputStream;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ethan Wang
 */
@SuperBuilder
@NoArgsConstructor
@Data
public class PutObjectReq extends BucketParam {
  /** object name or path/to/ */
  @NotBlank private String object;

  /** 文件输入流 */
  private InputStream stream;

  private String contentType;

  private Map<String, String> headers;
  private Map<String, String> userMetadata;

  /** -1 不知道输入流大小 */
  private long objectSize;

  private long partSize;

  public long getObjectSize() {
    return objectSize <= 0L ? -1L : objectSize;
  }

  public long getPartSize() {
    // valid part size must be provided when object size is unknown
    return objectSize <= 0L && partSize <= 0L ? 10485760L : partSize;
  }
}
