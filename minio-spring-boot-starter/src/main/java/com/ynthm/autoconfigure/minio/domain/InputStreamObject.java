package com.ynthm.autoconfigure.minio.domain;

import lombok.Data;

import java.io.InputStream;

/**
 * @author Ethan Wang
 */
@Data
public class InputStreamObject {
  private String objectName;
  private InputStream inputStream;
  private String contentType;

  public InputStreamObject(String objectName, InputStream inputStream, String contentType) {
    this.objectName = objectName;
    this.inputStream = inputStream;
    this.contentType = contentType;
  }

  public InputStreamObject(String objectName, InputStream inputStream) {
    this.objectName = objectName;
    this.inputStream = inputStream;
  }

  private long size = -1;
  /** 褰搒ize鏈煡鏃讹紝椤昏缃畃artSize锛屾帶鍒跺唴瀛樹娇鐢ㄣ�傞粯璁�10m */
  private long partSize = 10485760;
}
