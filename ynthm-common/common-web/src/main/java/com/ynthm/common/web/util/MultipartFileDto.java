package com.ynthm.common.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class MultipartFileDto implements MultipartFile {

  private final String name;
  private final String originalFilename;
  @Nullable
  private final String contentType;
  private final byte[] content;

  public MultipartFileDto(String name, @Nullable byte[] content) {
    this(name, "", null, content);
  }

  public MultipartFileDto(String name, InputStream contentStream) throws IOException {
    this(name, "", null, FileCopyUtils.copyToByteArray(contentStream));
  }

  public MultipartFileDto(String name, @Nullable String originalFilename, @Nullable String contentType, @Nullable byte[] content) {
    Assert.hasLength(name, "Name must not be empty");
    this.name = name;
    this.originalFilename = originalFilename != null ? originalFilename : "";
    this.contentType = contentType;
    this.content = content != null ? content : new byte[0];
  }

  public MultipartFileDto(String name, @Nullable String originalFilename, @Nullable String contentType, InputStream contentStream) throws IOException {
    this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getOriginalFilename() {
    return this.originalFilename;
  }

  @Nullable
  @Override
  public String getContentType() {
    return this.contentType;
  }

  @Override
  public boolean isEmpty() {
    return this.content.length == 0;
  }

  @Override
  public long getSize() {
    return this.content.length;
  }

  @Override
  public byte[] getBytes() throws IOException {
    return this.content;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(this.content);
  }

  @Override
  public void transferTo(File dest) throws IOException, IllegalStateException {
    FileCopyUtils.copy(this.content, dest);
  }
}
