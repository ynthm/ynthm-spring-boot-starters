package com.ynthm.spring.web.demo.excel.util;

import com.ynthm.spring.web.demo.excel.client.Dict;
import com.ynthm.spring.web.demo.excel.service.DictService;
import com.ynthm.spring.web.demo.web.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/** @author Ethan Wang */
public class DictHelper {

  public static final Logger log = LoggerFactory.getLogger(DictHelper.class);

  private DictService dictService = SpringUtil.getBean(DictService.class);

  private final Map<String, Map<String, Integer>> parentCodeLabel2Value = new HashMap<>();
  private final Map<String, Map<Integer, String>> parentCodeValue2Label = new HashMap<>();

  private static class SingletonHolder {
    private static final DictHelper INSTANCE = new DictHelper();
  }

  private DictHelper() {}

  public static DictHelper getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public Integer getValue(String parentCode, String label) {
    if (!parentCodeLabel2Value.containsKey(parentCode)) {
      fill(parentCode);
    }

    Map<String, Integer> label2Value = parentCodeLabel2Value.get(parentCode);
    if (label2Value.containsKey(label)) {
      return label2Value.get(label);
    }

    log.warn("dict type {} not contains label {}", parentCode, label);
    return null;
  }

  public String getLabel(String parentCode, Integer value) {

    if (!parentCodeValue2Label.containsKey(parentCode)) {
      fill(parentCode);
    }

    Map<Integer, String> value2Label = parentCodeValue2Label.get(parentCode);
    if (value2Label.containsKey(value)) {
      return value2Label.get(value);
    }

    log.warn("dict type {} not contains value {}", parentCode, value);
    return null;
  }

  /**
   * 通过 {@link com.ynthm.spring.web.demo.web.SpringUtil} huo
   *
   * @param parentCode
   */
  private void fill(String parentCode) {

    parentCodeLabel2Value.put(parentCode, new HashMap<>());
    parentCodeValue2Label.put(parentCode, new HashMap<>());
    // 填充数据 通过

    for (Dict dictData : dictService.listBy(parentCode)) {
      parentCodeLabel2Value
          .get(parentCode)
          .put(dictData.getLabel(), Integer.parseInt(dictData.getValue()));
      parentCodeValue2Label
          .get(parentCode)
          .put(Integer.parseInt(dictData.getValue()), dictData.getLabel());
    }
  }
}
