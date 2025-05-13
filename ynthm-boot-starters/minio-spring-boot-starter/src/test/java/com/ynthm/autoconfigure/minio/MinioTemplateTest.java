package com.ynthm.autoconfigure.minio;

import com.google.common.collect.Lists;
import com.ynthm.autoconfigure.minio.config.ContentType;
import com.ynthm.autoconfigure.minio.domain.*;
import com.ynthm.common.util.FileUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import okhttp3.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ethan Wang
 * @version 1.0
 */
class MinioTemplateTest {

  public static final String BUCKET_NAME = "bucket-1";

  private MinioClient minioClient;
  private MinioTemplate minioTemplate;

  @BeforeEach
  void setUp() {
    minioClient =
        MinioClient.builder()
            .endpoint("http://127.0.0.1:9000")
            .credentials("duzx65tsmp2485ZE", "Fppp5upuQlDhEIENSMl5DwVAzYagFlgp")
            .build();
    minioTemplate = new MinioTemplate(minioClient);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void bucketExists() {}

  @Test
  void makeBucket() {}

  @Test
  void listBuckets() {
    for (Bucket bucket : minioTemplate.listBuckets()) {
      System.out.println(bucket.name());
    }
  }

  @Test
  void getBucket() {}

  @Test
  void removeBucket() {}

  @Test
  void statObject() {}

  @Test
  void removeObject() {}

  @Test
  void testPreSignedObjectUrl() {}

  @Test
  void copyObject() {}

  @Test
  void removeObjects() {}

  @Test
  void setObjectTags() {}

  @Test
  void composeObject() {}

  @Test
  void testUploadSnowballObjects() {}

  @Test
  void createBucket() {
    BucketParam param = new BucketParam();
    param.setBucket(BUCKET_NAME);
    if (!minioTemplate.bucketExists(param)) {
      minioTemplate.makeBucket(param);
      Assertions.assertTrue(minioTemplate.bucketExists(param));
    }
  }

  @Test
  void createFolder() {
    BaseObject baseObject = new BaseObject();
    baseObject.setBucket(BUCKET_NAME);
    baseObject.setObject("abc/efg/");

    minioTemplate.removeObject(baseObject);
    ObjectWriteResponse folder = minioTemplate.createFolder(baseObject);
    System.out.println(folder.headers());
    Assertions.assertNotNull(folder.headers().get("ETAG"));
  }

  @Test
  void putObject() throws IOException {
    PutObjectReq req = new PutObjectReq();
    req.setBucket(BUCKET_NAME);
    req.setObject("abc.jpg");
    req.setContentType(ContentType.JPG.getValue());
    req.setStream(new ClassPathResource("alain-bonnardeaux.jpg").getInputStream());
    ObjectWriteResponse response = minioTemplate.putObject(req);
    System.out.println(response.headers());
    Assertions.assertNotNull(response.headers().get("ETAG"));
  }

  @Test
  void getObject() {
    GetObjectReq req = new GetObjectReq();
    req.setBucket(BUCKET_NAME);
    req.setObject("abc.jpg");
    minioTemplate.getObject(
        req,
        headers -> {},
        inputStream -> {
          Assertions.assertNotNull(inputStream);
          try {
            Files.copy(inputStream, Paths.get("/Users/ynthm/Downloads/file-003.jpg"));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Test
  void downloadAndUploadObject() throws Exception {

    minioClient.downloadObject(
        DownloadObjectArgs.builder()
            .bucket(BUCKET_NAME)
            .object("abc.png")
            .filename("/Users/ynthm/Downloads/my-object-file.png")
            .build());
    // Upload an JSON file.
    ObjectWriteResponse response =
        minioClient.uploadObject(
            UploadObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object("object-file-001.png")
                .filename("/Users/ynthm/Downloads/my-object-file.png")
                .contentType(ContentType.PNG.getValue())
                .build());

    Assertions.assertNotNull(response.headers().get("ETAG"));
  }

  @Test
  void preSignedObjectUrl() {
    String objectName = "alain.jpg";
    PreSignedReq req = new PreSignedReq();
    req.setBucket(BUCKET_NAME);
    req.setObject(objectName);

    req.setMethod(Method.GET);
    Map<String, String> reqParams = new HashMap<>();
    reqParams.put("response-content-type", "image/jpg");
    req.setReqParams(reqParams);

    String preSignedObjectUrl = minioTemplate.preSignedObjectUrl(req);
    System.out.println(preSignedObjectUrl);
    Assertions.assertNotNull(preSignedObjectUrl);
  }

  @Test
  void putPreSignedObjectUrl() throws IOException {

    String objectName = "abc.png";
    PreSignedReq req = new PreSignedReq();
    req.setBucket(BUCKET_NAME);
    req.setObject(objectName);
    req.setMethod(Method.PUT);

    Map<String, String> reqParams = new HashMap<>();
    reqParams.put("Content-Type", FileUtil.getMimeType(objectName));
    req.setReqParams(reqParams);

    minioTemplate.removeObject(req);

    String signedObjectUrl = minioTemplate.preSignedObjectUrl(req);
    File file = new ClassPathResource("alain-bonnardeaux.jpg").getFile();
    Request request =
        new Request.Builder().url(signedObjectUrl).put(RequestBody.create(file, null)).build();
    OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (response.isSuccessful()) {
        System.out.println(response.headers().get("ETAG"));
        Assertions.assertNotNull(response.headers().get("ETAG"));
      } else {
        System.out.println("Failed to upload");
      }
    }
  }

  @Test
  void headPreSignedObjectUrl() throws IOException {
    String objectName = "abc.png";
    PreSignedReq req = new PreSignedReq();
    req.setBucket(BUCKET_NAME);
    req.setObject(objectName);
    req.setMethod(Method.HEAD);
    String signedObjectUrl = minioTemplate.preSignedObjectUrl(req);
    System.out.println(signedObjectUrl);

    Request request = new Request.Builder().url(signedObjectUrl).head().build();
    OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
    try (Response response = httpClient.newCall(request).execute()) {
      if (response.isSuccessful()) {
        System.out.println(response.headers());
      } else {
        System.out.println("Failed to upload");
      }
    }

    StatObjectResponse statObjectResponse = minioTemplate.statObject(req);
    System.out.println(statObjectResponse.headers());
    Assertions.assertNotNull(statObjectResponse.headers().get("ETAG"));
  }

  @Test
  void preSignedPostFormData() throws IOException {
    String objectName = "alain.jpg";
    String bucketName = BUCKET_NAME;
    String mimeType = FileUtil.getMimeType(objectName);
    PreSignedFormDataReq baseObject = new PreSignedFormDataReq();
    baseObject.setBucket(bucketName);
    baseObject.setObject(objectName);
    Map<String, String> formData = minioTemplate.preSignedPostFormData(baseObject);

    Assertions.assertFalse(formData.isEmpty());

    // Upload an image using POST object with form-data.
    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
    multipartBuilder.setType(MultipartBody.FORM);

    for (Map.Entry<String, String> entry : formData.entrySet()) {
      multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
    }
    multipartBuilder.addFormDataPart("key", objectName);
    multipartBuilder.addFormDataPart("Content-Type", mimeType);

    // "file" must be added at last.
    multipartBuilder.addFormDataPart(
        "file",
        objectName,
        RequestBody.create(new ClassPathResource("alain-bonnardeaux.jpg").getFile(), null));

    Request request =
        new Request.Builder()
            .url("http://127.0.0.1:9000/" + bucketName)
            .post(multipartBuilder.build())
            .build();
    OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
    Response response = httpClient.newCall(request).execute();
    if (response.isSuccessful()) {
      System.out.println(response.headers().get("ETAG"));
      System.out.println("uploaded successfully using POST form");
    } else {
      System.out.println("Failed to upload");
    }
  }

  @Test
  void uploadSnowballObjects() throws IOException {
    List<InputStreamObject> list = Lists.newArrayList();
    for (int i = 1; i <= 3; i++) {
      String fileName = String.format("auntie-%d.png", i);
      list.add(
          new InputStreamObject(fileName, new ClassPathResource("auntie-1.png").getInputStream()));
    }

    UploadSnowballObjectsReq uploadSnowballObjectsReq = new UploadSnowballObjectsReq();
    uploadSnowballObjectsReq.setBucket(BUCKET_NAME);
    uploadSnowballObjectsReq.setObjects(list);
    minioTemplate.uploadSnowballObjects(uploadSnowballObjectsReq);
  }

  @Test
  void stats() throws Exception {
    StatObjectResponse statObjectResponse =
        minioClient.statObject(
            StatObjectArgs.builder().bucket(BUCKET_NAME).object("auntie-1.png").build());

    System.out.println(statObjectResponse.contentType());
  }
}
