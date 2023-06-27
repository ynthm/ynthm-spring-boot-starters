package com.ynthm.demo.minio;

import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import com.ynthm.autoconfigure.minio.MinioTemplate;
import com.ynthm.autoconfigure.minio.config.MinioClientProperties;
import com.ynthm.autoconfigure.minio.domain.*;
import com.ynthm.common.constant.StringPool;
import com.ynthm.common.domain.Result;
import com.ynthm.common.util.FileUtil;
import com.ynthm.common.util.id.IdUtil;
import com.ynthm.common.web.util.ServletUtil;
import com.ynthm.demo.minio.constant.MinioConst;
import com.ynthm.demo.minio.domain.ObjectItem;
import com.ynthm.demo.minio.domain.ObjectItemListReq;
import com.ynthm.demo.minio.domain.StatObjectResp;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

/**
 * @author Ethan Wang
 */
@Slf4j
@RestController
public class FileController {
  private MinioTemplate minioTemplate;

  private MinioClientProperties minioClientProperties;

  @Autowired
  public void setMinioClientProperties(MinioClientProperties minioClientProperties) {
    this.minioClientProperties = minioClientProperties;
  }

  @Resource
  public void setMinioTemplate(MinioTemplate minioTemplate) {
    this.minioTemplate = minioTemplate;
  }

  @PostMapping("/preSignedObjectUrl")
  public String preSignedObjectUrl(@Valid @RequestBody PreSignedReq req) {
    return minioTemplate.preSignedObjectUrl(req);
  }

  @GetMapping("/preSignedObjectUrl/{object}")
  public String preSignedObjectUrl(@PathVariable("object") String filename) {
    PreSignedReq req = new PreSignedReq();
    req.setMethod(Method.GET);
    req.setBucket(minioClientProperties.getDefaultBucket());
    req.setObject(filename);

    return minioTemplate.preSignedObjectUrl(req);
  }

  @GetMapping("/buckets/{bucketName}/preSignedObjectUrl/{object}")
  public String preSignedObjectUrl(
      @PathVariable("bucketName") String bucketName, @PathVariable("object") String filename) {
    PreSignedReq req = new PreSignedReq();
    req.setMethod(Method.GET);
    req.setBucket(bucketName);
    req.setObject(filename);
    return minioTemplate.preSignedObjectUrl(req);
  }

  @PostMapping("/preSignedObjectUrls")
  public @NotEmpty List<ObjectItem> fileUrls(@Validated @RequestBody ObjectItemListReq req) {
    preSignedRequest(req, minioClientProperties.getDefaultBucket());
    return req.getList();
  }

  @PostMapping("/buckets/{bucketName}/preSignedObjectUrls")
  public @NotEmpty List<ObjectItem> fileUrls(
      @PathVariable("bucketName") String bucketName,
      @Validated @RequestBody ObjectItemListReq req) {
    // 判断 bucket 为公共时直接返回
    if (bucketName.equals(minioClientProperties.getDefaultPublicBucket())) {
      for (ObjectItem objectItem : req.getList()) {
        objectItem.setUrl(
            minioClientProperties.getEndpoint()
                + File.separator
                + bucketName
                + File.separator
                + objectItem.getObject());
      }
    }

    preSignedRequest(req, bucketName);
    return req.getList();
  }

  private void preSignedRequest(ObjectItemListReq req, String bucketName) {
    for (ObjectItem objectItem : req.getList()) {
      PreSignedReq preSignedReq = new PreSignedReq();
      preSignedReq.setBucket(bucketName);
      preSignedReq.setObject(objectItem.getObject());
      preSignedReq.setMethod(Method.GET);
      objectItem.setUrl(minioTemplate.preSignedObjectUrl(preSignedReq));
    }
  }

  /**
   * multipart/form-data
   *
   * @param file
   * @return
   */
  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public Result<String> upload(HttpServletRequest request, @RequestPart("file") MultipartFile file)
      throws IOException {
    log.info("request character encoding: {}", request.getCharacterEncoding());

    String contentType = file.getContentType();
    log.info("file media type: {}", MediaType.parseMediaType(contentType));

    String originalFilename = file.getOriginalFilename();
    log.info("original file name: {}", originalFilename);
    log.info(
        "original file name: {}  ({} > {})",
        new String(
            originalFilename.getBytes(WebUtils.DEFAULT_CHARACTER_ENCODING), StandardCharsets.UTF_8),
        WebUtils.DEFAULT_CHARACTER_ENCODING,
        StandardCharsets.UTF_8.name());
    log.info("file mime type: {}", request.getServletContext().getMimeType(originalFilename));
    log.info(
        "file mime type: {}",
        MediaTypeFactory.getMediaType(originalFilename).orElse(MediaType.ALL));

    String generatedName =
        IdUtil.nextId() + StringPool.DOT + FileUtil.getExtension(file.getOriginalFilename());
    Map<String, String> userMetadata = new HashMap<>();
    userMetadata.put(MinioConst.FILENAME, file.getOriginalFilename());
    try {
      ObjectWriteResponse objectWriteResponse =
          minioTemplate.putObject(
              PutObjectReq.builder()
                  .bucket(minioClientProperties.getDefaultBucket())
                  .object(generatedName)
                  .contentType(contentType)
                  .userMetadata(userMetadata)
                  .stream(file.getInputStream())
                  .objectSize(file.getSize())
                  .build());

      System.out.println(objectWriteResponse.etag());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    HttpServletResponse response =
        ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getResponse();
    assert response != null;
    log.info("response charset: {}", response.getCharacterEncoding());
    return Result.ok(generatedName);
  }

  @PostMapping(value = "/buckets/{bucketName}/upload")
  public Result<String> upload(
      HttpServletRequest request,
      @NotBlank @PathVariable("bucketName") String bucketName,
      @NotNull @RequestPart("file") MultipartFile file)
      throws IOException {

    String generatedName =
        IdUtil.nextId() + StringPool.DOT + FileUtil.getExtension(file.getOriginalFilename());
    // header 暂时不知道效果
    Map<String, String> headers = new HashMap<>();
    headers.put(HttpHeaders.USER_AGENT, request.getHeader(HttpHeaders.USER_AGENT));
    Map<String, String> userMetadata = new HashMap<>();
    userMetadata.put(MinioConst.FILENAME, file.getOriginalFilename());

    minioTemplate.putObject(
        PutObjectReq.builder()
            .bucket(bucketName)
            .headers(headers)
            .userMetadata(userMetadata)
            .object(generatedName)
            .stream(file.getInputStream())
            .contentType(file.getContentType())
            .objectSize(file.getSize())
            .build());

    return Result.ok(generatedName);
  }

  @PostMapping("/upload/multi")
  public void multiUpload(@RequestParam("files") MultipartFile[] files) throws Exception {
    multiUploadFiles(minioClientProperties.getDefaultBucket(), files);
  }

  @PostMapping("/buckets/{bucketName}/upload/multi")
  public void multiUpload(
      @PathVariable("bucketName") String bucketName, @RequestParam("files") MultipartFile[] files)
      throws IOException {
    multiUploadFiles(bucketName, files);
  }

  private void multiUploadFiles(String bucketName, MultipartFile[] files) throws IOException {
    List<InputStreamObject> list = Lists.newArrayList();

    for (MultipartFile file : files) {
      list.add(
          new InputStreamObject(
              file.getOriginalFilename(), file.getInputStream(), file.getContentType()));
    }

    UploadSnowballObjectsReq uploadSnowballObjectsReq = new UploadSnowballObjectsReq(list);
    uploadSnowballObjectsReq.setBucket(bucketName);
    minioTemplate.uploadSnowballObjects(uploadSnowballObjectsReq);
  }

  @PostMapping("/stat")
  public Result<StatObjectResp> stat(@Validated @RequestBody BaseObject baseObject) {
    StatObjectResponse statObjectResponse = minioTemplate.statObject(baseObject);
    StatObjectResp statObjectResp = new StatObjectResp();
    statObjectResp.setEtag(statObjectResponse.etag());
    statObjectResp.setSize(statObjectResponse.size());
    statObjectResp.setUserMetadata(statObjectResponse.userMetadata());
    statObjectResp.setHeaders(statObjectResponse.headers().toMultimap());
    return Result.ok(statObjectResp);
  }

  /**
   * 下载
   *
   * <p>注意这个地方必须放回 void 不然无法根据Content-Type转换返回值 或者通过 response.reset() 置空 ContentType
   *
   * @param response
   * @param filename
   */
  @RequestMapping("/download")
  public void fileDownLoad(
      HttpServletResponse response, @RequestParam("filename") String filename) {
    fileDownLoad(minioClientProperties.getDefaultBucket(), filename, response);
  }

  @RequestMapping("/buckets/{bucketName}/objects/download")
  public void fileDownLoad(
      @NotBlank @PathVariable("bucketName") String bucketName,
      HttpServletResponse response,
      @RequestParam("filename") String filename) {
    // /buckets/bucket-1/objects/download
    fileDownLoad(bucketName, filename, response);
  }

  public void fileDownLoad(String bucketName, String object, HttpServletResponse response) {
    BaseObject baseObject = new BaseObject();
    baseObject.setBucket(bucketName);
    baseObject.setObject(object);
    StatObjectResponse statObjectResponse = minioTemplate.statObject(baseObject);
    // 文件不存在直接报错
    String filename = statObjectResponse.userMetadata().getOrDefault(MinioConst.FILENAME, object);

    response.reset();
    ServletUtil.responseForDownload(response, statObjectResponse.contentType(), filename);

    GetObjectReq getObjectReq = new GetObjectReq();
    getObjectReq.setBucket(bucketName);
    getObjectReq.setObject(object);
    minioTemplate.getObject(
        getObjectReq,
        headers -> {
          log.debug(headers.toString());
        },
        stream -> {
          try {
            FileCopyUtils.copy(stream, response.getOutputStream());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }
}
