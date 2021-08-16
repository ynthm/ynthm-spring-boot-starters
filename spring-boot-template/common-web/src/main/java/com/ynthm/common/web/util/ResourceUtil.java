package com.ynthm.common.web.util;

import java.io.InputStream;

/**
 * @author Ethan Wang
 */
public class ResourceUtil {
  private ResourceUtil(){}

  public static InputStream getResourceAsStream(Class<?> clazz, String resourceLocation){
    return clazz
            .getClassLoader()
            .getResourceAsStream(resourceLocation);
  }
}
