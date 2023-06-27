package com.ynthm.common.web.validate;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 */
@Data
@Accessors(chain = true)
public class BindError {
  private String field;
  private String code;
  private String message;
}
