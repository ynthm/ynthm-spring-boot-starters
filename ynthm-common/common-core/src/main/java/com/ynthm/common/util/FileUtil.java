package com.ynthm.common.util;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Paths;

/**
 * MimetypesFileTypeMap .getDefaultFileTypeMap() .getContentType(attachment.getFileName());
 *
 * @author Ynthm Wang
 * @version 1.0
 */
public class FileUtil {
  private FileUtil() {}

  /**
   * Returns the extension (i.e. the part after the last ".") of a file.
   *
   * <p>Will return an empty string if the file name doesn't contain any dots. Only the last segment
   * of a the file name is consulted - i.e. all leading directories of the {@code filename}
   * parameter are skipped.
   *
   * @return the extension of filename
   * @param filename the name of the file to obtain the extension of.
   */
  public static String getExtension(final String filename) {
    if (filename == null) {
      return null;
    }

    final String name = new File(filename).getName();
    final int extensionPosition = name.lastIndexOf('.');
    if (extensionPosition < 0) {
      return "";
    }
    return name.substring(extensionPosition + 1);
  }

  /**
   * Returns the basename (i.e. the part up to and not including the last ".") of the last path
   * segment of a filename.
   *
   * <p>Will return the file name itself if it doesn't contain any dots. All leading directories of
   * the {@code filename} parameter are skipped.
   *
   * @return the basename of filename
   * @param filename the name of the file to obtain the basename of.
   */
  public static String getBaseName(final String filename) {
    if (filename == null) {
      return null;
    }

    final String name = new File(filename).getName();

    final int extensionPosition = name.lastIndexOf('.');
    if (extensionPosition < 0) {
      return name;
    }

    return name.substring(0, extensionPosition);
  }

  public static String getMimeType(String filePath) {
    String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
    if (null == contentType) {
      // 补充一些常用的mimeType
      if (filePath.endsWith(".css")) {
        contentType = "text/css";
      } else if (filePath.endsWith(".js")) {
        contentType = "application/x-javascript";
      }
    }

    // 补充
    if (null == contentType) {
      contentType = getMimeType(Paths.get(filePath).toString());
    }

    return contentType;
  }
}
