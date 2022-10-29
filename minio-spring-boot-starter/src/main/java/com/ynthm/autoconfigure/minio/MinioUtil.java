package com.ynthm.autoconfigure.minio;

import com.ynthm.autoconfigure.minio.domain.*;
import com.ynthm.common.exception.UtilException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import okhttp3.Headers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Ethan Wang
 */
public class MinioUtil {

  private final MinioClient minioClient;

  public MinioUtil(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  public boolean bucketExists(String bucket) {
    try {
      return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public List<Bucket> listBuckets() {
    try {
      return minioClient.listBuckets();
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  /**
   * 获取桶
   *
   * @param bucket 桶名字
   * @return 可选
   */
  public Optional<Bucket> getBucket(String bucket) {
    // Create bucket with default region.
    return listBuckets().stream().filter(b -> b.name().equals(bucket)).findFirst();
  }

  /**
   * Create bucket with default region.
   *
   * @param bucket 桶名字
   */
  public void makeBucket(String bucket) {
    try {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public void removeBucket(String bucket) {
    try {
      minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public ObjectWriteResponse createFolder(BaseObject req) {
    try {
      // Create object ends with '/' (also called as folder or directory).
      return minioClient.putObject(
          PutObjectArgs.builder().bucket(req.getBucket()).object(req.getObject()).stream(
                  new ByteArrayInputStream(new byte[] {}), 0, -1)
              .build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public ObjectWriteResponse putObject(InputStream inputStream, PutObjectReq req) {

    PutObjectArgs.Builder argBuilder =
        PutObjectArgs.builder().bucket(req.getBucket()).object(req.getObject());
    if (Objects.nonNull(req.getContentType())) {
      argBuilder.contentType(req.getContentType().getValue());
    }

    if (Objects.nonNull(req.getHeaders())) {
      argBuilder.headers(req.getHeaders());
    }

    if (Objects.nonNull(req.getUserMetadata())) {
      argBuilder.userMetadata(req.getUserMetadata());
    }

    argBuilder.stream(inputStream, req.getObjectSize(), req.getPartSize());

    try {
      return minioClient.putObject(argBuilder.build());

    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  /**
   * get object given the bucket and object name
   *
   * @param req
   */
  public InputStream getObject(GetObjectReq req, Consumer<Headers> headersConsumer) {
    GetObjectArgs.Builder builder =
        GetObjectArgs.builder().bucket(req.getBucket()).object(req.getObject());
    if (req.getOffset() != -1) {
      builder.offset(req.getOffset());
    }
    if (req.getLength() != -1) {
      builder.length(req.getLength());
    }

    try {
      // 在外面关闭流
      GetObjectResponse response = minioClient.getObject(builder.build());
      headersConsumer.accept(response.headers());
      // Read data from stream
      return response;
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public StatObjectResponse statObject(BaseObject baseObject) {
    try {
      return minioClient.statObject(
          StatObjectArgs.builder()
              .bucket(baseObject.getBucket())
              .object(baseObject.getObject())
              .build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  /** Remove object. */
  public void removeObject(BaseObject baseObject) {
    try {
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(baseObject.getBucket())
              .object(baseObject.getObject())
              .build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  /**
   * Get pre signed URL
   *
   * <p>Method.GET 临时访问文件
   *
   * <p>Method.PUT 临时上传文件
   *
   * @param req 桶中对象
   * @return url
   */
  public String preSignedObjectUrl(PreSignedReq req) {
    try {

      GetPresignedObjectUrlArgs.Builder builder =
          GetPresignedObjectUrlArgs.builder()
              .method(req.getMethod())
              .bucket(req.getBucket())
              .object(req.getObject())
              .expiry(req.getDuration(), req.getUnit());
      if (Objects.nonNull(req.getReqParams())) {
        builder.extraQueryParams(req.getReqParams());
      }

      return minioClient.getPresignedObjectUrl(builder.build());
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }

  public Map<String, String> preSignedPostFormData(PreSignedFormDataReq req) {
    String bucketName = req.getBucket();
    // Create new post policy for 'bucketName' with 7 days expiry from now.
    PostPolicy policy = new PostPolicy(bucketName, req.getExpiration());
    // Add condition that 'key' (object name) equals to 'objectName'.
    policy.addEqualsCondition("key", req.getObject());
    // Add condition that 'Content-Type' starts with 'image/'.
    policy.addStartsWithCondition("Content-Type", req.getContentTypeStartsWith());
    // Add condition that 'content-length-range' is between 64kiB to 10MiB.
    policy.addContentLengthRangeCondition(req.getLowerLimit(), req.getUpperLimit());
    try {
      return minioClient.getPresignedPostFormData(policy);
    } catch (ErrorResponseException
        | InsufficientDataException
        | InternalException
        | InvalidKeyException
        | InvalidResponseException
        | IOException
        | NoSuchAlgorithmException
        | ServerException
        | XmlParserException e) {
      throw new UtilException(e);
    }
  }
}
