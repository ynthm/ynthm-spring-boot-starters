package com.ynthm.autoconfigure.push.domain.model;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Option {
  private boolean apnsProduction;
  private Map<String, JsonObject> thirdPartyChannel;
}
