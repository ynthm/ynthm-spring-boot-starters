package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.io.InputStream;
import java.util.Map;

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
  private long objectSize = -1L;

  private long partSize = objectSize == -1L ? 10485760L : -1L;
}
