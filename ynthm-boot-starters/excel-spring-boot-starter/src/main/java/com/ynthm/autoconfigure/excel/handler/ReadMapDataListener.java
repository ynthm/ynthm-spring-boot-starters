package com.ynthm.autoconfigure.excel.handler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板的读取类
 *
 * @author Ethan Wang
 */
@Slf4j
public class ReadMapDataListener extends ReadDataListener<LinkedHashMap<Integer, String>> {

  public ReadMapDataListener(Consumer<List<LinkedHashMap<Integer, String>>> consumer) {
    super(consumer);
  }
}
