package com.ynthm.autoconfigure.minio.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author Ethan Wang
 */
@Getter
public enum ContentType {
  /** 二进制数组 */
  BINARY("application/octet-stream"),

  MP3("audio/mp3"),

  MP4("video/mp4"),

  PNG("image/png"),

  JPEG("image/jpeg"),

  JPG("image/jpg"),

  PPT("applications-powerpoint"),

  PDF("application/pdf"),

  TXT_XML("text/xml");

  @JsonValue private final String value;

  ContentType(String name) {
    this.value = name;
  }
}
