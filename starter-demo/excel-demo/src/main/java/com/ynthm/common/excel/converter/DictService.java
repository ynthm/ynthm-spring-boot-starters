package com.ynthm.common.excel.converter;

import com.ynthm.common.excel.domain.Dict;

import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public interface DictService {
  List<Dict> listBy(String parentCode);
}
