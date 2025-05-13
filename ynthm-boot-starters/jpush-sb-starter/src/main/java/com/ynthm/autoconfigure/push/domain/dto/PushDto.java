package com.ynthm.autoconfigure.push.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PushDto {
  public long msgId;
  public int sendNo;
}
