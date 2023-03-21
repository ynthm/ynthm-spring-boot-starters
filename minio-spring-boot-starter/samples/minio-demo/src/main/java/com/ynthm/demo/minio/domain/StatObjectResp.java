package com.ynthm.demo.minio.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class StatObjectResp {
  private String etag;
  private long size;
  private Map<String, String> userMetadata;
  private Map<String, List<String>> headers;
}
