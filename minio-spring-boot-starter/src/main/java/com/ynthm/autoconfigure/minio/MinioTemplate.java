package com.ynthm.autoconfigure.minio;

import com.ynthm.autoconfigure.minio.domain.*;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectResponse;
import okhttp3.Headers;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author Ethan Wang
 */
public class MinioTemplate implements MinioOperations {

  private final MinioUtil minioUtil;

  public MinioTemplate(MinioUtil minioUtil) {
    this.minioUtil = minioUtil;
  }

  @Override
  public boolean bucketExists(BucketParam bucketParam) {
    return minioUtil.bucketExists(bucketParam);
  }

  @Override
  public void makeBucket(BucketParam bucketParam) {
    minioUtil.makeBucket(bucketParam);
  }

  @Override
  public ObjectWriteResponse putObject(PutObjectReq req) {
    return minioUtil.putObject(req);
  }

  @Override
  public ObjectWriteResponse uploadSnowballObjects(UploadSnowballObjectsReq req) {
    return minioUtil.uploadSnowballObjects(req);
  }

  @Override
  public void getObject(
      GetObjectReq req, Consumer<Headers> headersConsumer, Consumer<InputStream> readStream) {
    minioUtil.getObject(req, headersConsumer, readStream);
  }

  @Override
  public StatObjectResponse statObject(BaseObject baseObject) {
    return minioUtil.statObject(baseObject);
  }

  @Override
  public String preSignedObjectUrl(PreSignedReq req) {
    return minioUtil.preSignedObjectUrl(req);
  }

  @Override
  public void removeObject(BaseObject baseObject) {
    minioUtil.removeObject(baseObject);
  }
}
