package com.ynthm.demo.minio;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Slf4j
public class MultipartFileUtil {
  private MultipartFileUtil() {
  }

  public static MultipartFile multipartFile(InputStream is, String fileName) throws IOException {
    return multipartFile(is, "file", fileName);
  }

  public static MultipartFile multipartFile(InputStream is, String fieldName, String fileName) throws IOException {
    return new CommonsMultipartFile(createFileItem(is, fieldName, fileName));
  }

  public static FileItem createFileItem(InputStream is, String fieldName, String fileName) throws IOException {
    // 小于 5M 文件都在内存中,否则存入硬盘
    final int tmpFileSize = 5242880;
    // 默认 repository java.io.tmpdir
    DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(tmpFileSize, null);

    Optional.ofNullable(ContextLoader.getCurrentWebApplicationContext())
            .map(WebApplicationContext::getServletContext)
            .map(FileCleanerCleanup::getFileCleaningTracker).ifPresent(
                    diskFileItemFactory::setFileCleaningTracker
            );

    FileItem fileItem = diskFileItemFactory
            .createItem(fieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
    IOUtils.copy(is, fileItem.getOutputStream());
    return fileItem;
  }
}
