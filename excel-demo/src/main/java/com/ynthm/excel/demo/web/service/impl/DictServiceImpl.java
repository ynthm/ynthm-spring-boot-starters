package com.ynthm.excel.demo.web.service.impl;

import com.ynthm.excel.demo.web.client.Dict;
import com.ynthm.excel.demo.web.service.DictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Service
public class DictServiceImpl implements DictService {
  @Override
  public List<Dict> listBy(String parentCode) {
    return new ArrayList<>();
  }
}
