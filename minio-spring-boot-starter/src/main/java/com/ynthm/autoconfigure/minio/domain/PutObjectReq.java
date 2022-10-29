package com.ynthm.autoconfigure.minio.domain;

import com.ynthm.autoconfigure.minio.config.ContentType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author Ethan Wang
 */
@Data
public class PutObjectReq {
  @NotBlank private String bucket;
  /** object name or path/to/ */
  @NotBlank private String object;

  private ContentType contentType;

  private Map<String, String> headers;
  private Map<String, String> userMetadata;

  /** -1 不知道输入流大小 */
  private long objectSize = -1L;

  private long partSize = objectSize == -1L ? 10485760L : -1L;
}
