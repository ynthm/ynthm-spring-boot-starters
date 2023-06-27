package com.ynthm.common.context.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return applicationContext.getBean(name, requiredType);
  }

  public static boolean containsBean(String name){
    return applicationContext.containsBean(name);
  }

  public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException{
    return applicationContext.isSingleton(name);
  }

  @Nullable
  public static Class<?> getType(String name) throws NoSuchBeanDefinitionException{
    return applicationContext.getType(name);
  }
  
  public static String[] getAliases(String name){
    return applicationContext.getAliases(name);
  }
}
