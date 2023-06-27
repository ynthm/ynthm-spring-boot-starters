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
public interface S3Operations {
  /**
   * 判断 bucket 是否存在
   *
   * @return 结果
   */
  boolean bucketExists(BucketParam bucketParam);

  /**
   * 创建 bucket
   *
   */
  void makeBucket(BucketParam bucketParam);

  /**
   * 上传文件流
   *
   * @param req 请求参数
   * @return 相应结果
   */
  ObjectWriteResponse putObject(PutObjectReq req);

  /**
   * 批量上传
   *
   * @param req
   * @return
   */
  ObjectWriteResponse uploadSnowballObjects(UploadSnowballObjectsReq req);

  /**
   * 下载对象
   *
   * @param req 请求参数
   * @param headersConsumer 对象的 header 信息
   * @param readStream 消费对象输入流
   */
  void getObject(
      GetObjectReq req, Consumer<Headers> headersConsumer, Consumer<InputStream> readStream);

  /**
   * 查询对象状态 不存在会报错
   *
   * @param req 请求参数
   * @return 响应结果
   */
  StatObjectResponse statObject(BaseObject req);

  /**
   * 获取 pre signed url 来
   *
   * <p>Method.GET 临时访问文件
   *
   * <p>Method.PUT 预签名上传文件
   *
   * @param req 请求参数
   * @return url
   */
  String preSignedObjectUrl(PreSignedReq req);

  /**
   * 删除对象
   *
   * @param req 请求参数
   */
  void removeObject(BaseObject req);
}
