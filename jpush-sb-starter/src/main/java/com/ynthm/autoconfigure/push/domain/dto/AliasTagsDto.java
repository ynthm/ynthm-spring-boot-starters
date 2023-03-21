package com.ynthm.autoconfigure.push.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class AliasTagsDto {
  private List<String> tags;
  private String alias;
}
