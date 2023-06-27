package com.ynthm.common.util;

import com.ynthm.common.exception.UtilException;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class ResourceUtil {

  private ResourceUtil() {}

  public static InputStream getResourceAsStream(String name) {
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
  }

  public static Path getResourcePath(String name) {
    try {
      return Paths.get(
          Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(name))
              .toURI());
    } catch (URISyntaxException e) {
      throw new UtilException(e);
    }
  }
}
