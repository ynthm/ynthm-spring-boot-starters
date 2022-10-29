package com.ynthm.autoconfigure.minio;

import com.ynthm.autoconfigure.minio.domain.BaseObject;
import com.ynthm.autoconfigure.minio.domain.GetObjectReq;
import com.ynthm.autoconfigure.minio.domain.PreSignedReq;
import com.ynthm.autoconfigure.minio.domain.PutObjectReq;
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
  public boolean bucketExists(String bucket) {
    return minioUtil.bucketExists(bucket);
  }

  @Override
  public void makeBucket(String bucket) {
    minioUtil.makeBucket(bucket);
  }

  @Override
  public ObjectWriteResponse putObject(InputStream inputStream, PutObjectReq req) {
    return minioUtil.putObject(inputStream, req);
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
