package com.ynthm.spring.web.demo.excel.service;

import com.ynthm.spring.web.demo.excel.client.Dict;

import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public interface DictService {

  List<Dict> listBy(String parentCode);
}
