package com.ynthm.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public class BeanUtil {

  public static <T> void setPropertyIntroSpector(
      Class<T> clazz, T bean, String propertyName, Object value) throws Exception {
    BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
    PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();
    if (proDescriptors != null) {
      for (PropertyDescriptor propDesc : proDescriptors) {
        if (propDesc.getName().equals(propertyName)) {
          Method methodSetUserName = propDesc.getWriteMethod();
          methodSetUserName.invoke(bean, value);
          break;
        }
      }
    }
  }

  public static <T> Object getPropertyIntroSpector(Class<T> clazz, T bean, String propertyName)
      throws IntrospectionException, InvocationTargetException, IllegalAccessException {
    BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

    PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();
    if (proDescriptors != null) {
      for (PropertyDescriptor propDesc : proDescriptors) {
        if (propDesc.getName().equals(propertyName)) {
          Method methodGetUserName = propDesc.getReadMethod();
          return methodGetUserName.invoke(bean);
        }
      }
    }

    return null;
  }

  public static <T> void setProperty(Class<T> clazz, T bean, String propertyName, Object value)
      throws Exception {
    PropertyDescriptor propDesc = new PropertyDescriptor(propertyName, clazz);
    Method methodSetUserName = propDesc.getWriteMethod();
    methodSetUserName.invoke(bean, value);
  }

  public static <T> Object getProperty(Class<T> clazz, T bean, String propertyName)
      throws Exception {
    PropertyDescriptor proDescriptor = new PropertyDescriptor(propertyName, clazz);
    Method methodGetUserName = proDescriptor.getReadMethod();
    return methodGetUserName.invoke(bean);
  }
}
