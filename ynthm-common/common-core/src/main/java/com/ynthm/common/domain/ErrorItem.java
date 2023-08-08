package com.ynthm.common.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
  protected String code;
  protected String msg;
}
