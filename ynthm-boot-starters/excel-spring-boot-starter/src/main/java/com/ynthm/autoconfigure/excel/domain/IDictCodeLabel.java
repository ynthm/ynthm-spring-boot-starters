package com.ynthm.autoconfigure.excel.domain;

/**
 * @author Ethan Wang
 * @version 1.0
 */
public interface IDictCodeLabel {

  String label(String parentCode, Integer code);

  Integer code(String parentCode, String label);
}
