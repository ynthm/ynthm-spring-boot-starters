package com.ynthm.common.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 详细错误信息
 *
 * @author Ethan Wang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorItem implements Serializable {
  /** code or key */
  protected String code;

  /** message or value */
  protected String msg;
}
