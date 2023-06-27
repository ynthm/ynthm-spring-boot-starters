package com.ynthm.demo.minio.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class AddObjectReq {
  private String region;
  private String bucket;
  @NotBlank
  private String object;
  private String category;
  private String businessKey;
  private String firstLevelFolder;
  @NotNull
  private MultipartFile file;
  private String remark;
}
