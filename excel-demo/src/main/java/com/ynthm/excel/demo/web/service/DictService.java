package com.ynthm.excel.demo.web.service;

import com.ynthm.excel.demo.web.client.Dict;

import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public interface DictService {

  List<Dict> listBy(String parentCode);
}
