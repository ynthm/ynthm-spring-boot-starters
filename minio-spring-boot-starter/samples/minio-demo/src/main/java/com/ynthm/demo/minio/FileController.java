package com.ynthm.demo.minio;

import com.google.common.collect.Lists;
import com.ynthm.autoconfigure.minio.MinioTemplate;
import com.ynthm.autoconfigure.minio.config.MinoClientProperties;
import com.ynthm.autoconfigure.minio.domain.*;
import com.ynthm.common.domain.Result;
import com.ynthm.common.web.util.ServletUtil;
import com.ynthm.demo.minio.domain.ObjectItem;
import com.ynthm.demo.minio.domain.ObjectItemListReq;
import io.minio.ObjectWriteResponse;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @author Ethan Wang
 */
@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {
  private MinioTemplate minioTemplate;

  @Autowired private MinoClientProperties minoClientProperties;

  @Resource
  public void setMinioTemplate(MinioTemplate minioTemplate) {
    this.minioTemplate = minioTemplate;
  }

  @PostMapping("/preSignedObjectUrl")
  public String preSignedObjectUrl(@Valid @RequestBody PreSignedReq req) {
    req.setMethod(Method.GET);
    return minioTemplate.preSignedObjectUrl(req);
  }

  @GetMapping("/preSignedObjectUrl/{object}")
  public String share(@PathVariable("object") String fileName) {
    PreSignedReq req = new PreSignedReq();
    req.setBucket(minoClientProperties.getDefaultBucketName());
    req.setObject(fileName);
    req.setMethod(Method.GET);
    return minioTemplate.preSignedObjectUrl(req);
  }

  @PostMapping("/preSignedObjectUrls")
  public @NotEmpty List<ObjectItem> fileUrls(@RequestBody ObjectItemListReq req) {
    preSignedRequest(req, minoClientProperties.getDefaultBucketName());
    return req.getList();
  }

  @PostMapping("/buckets/{bucketName}/preSignedObjectUrls")
  public @NotEmpty List<ObjectItem> fileUrls(
      @PathVariable("bucketName") String bucketName, @RequestBody ObjectItemListReq req) {
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
  public Result<Void> upload(HttpServletRequest request, @RequestPart("file") MultipartFile file)
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

    try {
      ObjectWriteResponse objectWriteResponse =
          minioTemplate.putObject(
              PutObjectReq.builder()
                  .bucket(minoClientProperties.getDefaultBucketName())
                  .object(file.getOriginalFilename())
                  .contentType(contentType)
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
    return Result.ok();
  }

  @PostMapping(value = "/buckets/{bucketName}/upload")
  public Result<Void> upload(
      @NotNull @RequestPart("file") MultipartFile file,
      @NotBlank @PathVariable("bucketName") String bucketName)
      throws IOException {

    minioTemplate.putObject(
        PutObjectReq.builder().bucket(bucketName).object(file.getOriginalFilename()).stream(
                file.getInputStream())
            .contentType(file.getContentType())
            .objectSize(file.getSize())
            .build());

    return Result.ok();
  }

  @PostMapping("/upload/multi")
  public void multiUpload(@RequestParam("files") MultipartFile[] files) throws Exception {
    multiUploadFiles(minoClientProperties.getDefaultBucketName(), files);
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

  @RequestMapping("/download")
  public String fileDownLoad(
      HttpServletResponse response, @RequestParam("fileName") String fileName) {

    // stat 下载文件不存在
    GetObjectReq getObjectReq = new GetObjectReq();
    getObjectReq.setBucket(minoClientProperties.getDefaultBucketName());
    getObjectReq.setObject(fileName);
    minioTemplate.getObject(
        getObjectReq,
        headers -> {
          System.out.println(headers);
        },
        stream -> {
          try {
            FileCopyUtils.copy(stream, response.getOutputStream());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    response.reset();
    ServletUtil.responseForDownload(response, fileName);

    //    response.reset();
    //    response.setContentType("application/octet-stream");
    //    response.setCharacterEncoding("utf-8");
    //    response.setContentLength((int) file.length());
    //    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

    //    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)); ) {
    //      byte[] buff = new byte[1024];
    //      OutputStream os = response.getOutputStream();
    //      int i = 0;
    //      while ((i = bis.read(buff)) != -1) {
    //        os.write(buff, 0, i);
    //        os.flush();
    //      }
    //    } catch (IOException e) {
    //      log.error("{}", e);
    //      return "下载失败";
    //    }
    return "下载成功";
  }

  public void downlo() {
    // /buckets/bucket-1/objects/download
  }

  public static void main(String[] args) throws UnsupportedEncodingException {
    ContentDisposition contentDisposition =
        ContentDisposition.builder("form-data")
            .filename("转 正.docx", StandardCharsets.UTF_8)
            .name("file")
            .build();
    System.out.println(contentDisposition);
    ContentDisposition disposition = ContentDisposition.parse(contentDisposition.toString());
    String file = URLEncoder.encode("转 正.docx", StandardCharsets.UTF_8.name());
    System.out.println(file);
    System.out.println(URLDecoder.decode(file, StandardCharsets.UTF_8.name()));
    System.out.println(URLEncoder.encode("hello world", StandardCharsets.UTF_8.name()));
  }
}
