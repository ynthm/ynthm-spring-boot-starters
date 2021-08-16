package com.ynthm.excel.demo.cdc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
@Component
public class CdcServiceHolder {
  @Autowired private List<CdcService> list = new ArrayList<>();

  public CdcService getByClass(Object obj) {
    for (CdcService service : list) {
      Class<?> beanClass = service.getBeanClass();
      if (beanClass.isInstance(obj)) {
        return service;
      }
    }
    return null;
  }
}
