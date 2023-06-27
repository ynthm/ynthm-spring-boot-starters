package com.ynthm.common.util;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class PeriodDay {
  private long days;
  private long hours;
  private long minutes;
  private long seconds;
}
