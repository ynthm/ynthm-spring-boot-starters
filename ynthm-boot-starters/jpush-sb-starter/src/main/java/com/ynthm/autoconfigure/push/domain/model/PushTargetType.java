package com.ynthm.autoconfigure.push.domain.model;

import lombok.Getter;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Getter
public enum PushTargetType {
  registration_id, alias, tag, broadcast
}
