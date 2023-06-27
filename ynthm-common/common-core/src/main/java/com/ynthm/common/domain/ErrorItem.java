package com.ynthm.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 详细错误信息
 *
 * @author Ethan Wang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorItem implements Serializable {
  private String code;
  private String msg;
}
