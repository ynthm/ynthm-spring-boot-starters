package com.ynthm.common.util;

import com.ynthm.common.exception.BaseException;

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

  private BeanUtil() {}

  public static <T> void setPropertyIntroSpector(
      Class<T> clazz, T bean, String propertyName, Object value) {
    BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(clazz);
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
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      throw new BaseException(e);
    }
  }

  public static Object getPropertyIntroSpector(Class<?> clazz, Object bean, String propertyName) {
    BeanInfo beanInfo;
    try {
      beanInfo = Introspector.getBeanInfo(clazz);
      PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();
      if (proDescriptors != null) {
        for (PropertyDescriptor propDesc : proDescriptors) {
          if (propDesc.getName().equals(propertyName)) {
            Method methodGetUserName = propDesc.getReadMethod();
            return methodGetUserName.invoke(bean);
          }
        }
      }
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      throw new BaseException(e);
    }

    return null;
  }

  public static <T> void setProperty(
      Class<T> clazz, Object bean, String propertyName, Object value) {
    PropertyDescriptor propDesc = null;
    try {
      propDesc = new PropertyDescriptor(propertyName, clazz);
      Method methodSetUserName = propDesc.getWriteMethod();
      methodSetUserName.invoke(bean, value);
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      throw new BaseException(e);
    }
  }

  public static <T> Object getProperty(Class<T> clazz, Object bean, String propertyName) {
    PropertyDescriptor proDescriptor;
    try {
      proDescriptor = new PropertyDescriptor(propertyName, clazz);
      Method methodGetUserName = proDescriptor.getReadMethod();
      return methodGetUserName.invoke(bean);
    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
      throw new BaseException(e);
    }
  }
}
