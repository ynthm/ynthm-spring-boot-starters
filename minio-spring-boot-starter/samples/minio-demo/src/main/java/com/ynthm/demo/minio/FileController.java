package com.ynthm.demo.minio;

import com.ynthm.autoconfigure.minio.MinioTemplate;
import com.ynthm.autoconfigure.minio.domain.PreSignedReq;
import io.minio.http.Method;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Ethan Wang
 */
@RestController
@RequestMapping("/files")
public class FileController {
  private MinioTemplate minioTemplate;

  @Resource
  public void setMinioTemplate(MinioTemplate minioTemplate) {
    this.minioTemplate = minioTemplate;
  }

  @PostMapping("/preSignedObjectUrl")
  public String preSignedObjectUrl(@Valid @RequestBody PreSignedReq req) {
    req.setMethod(Method.GET);
    return minioTemplate.preSignedObjectUrl(req);
  }
}
