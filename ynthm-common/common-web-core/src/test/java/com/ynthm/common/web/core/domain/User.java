package com.ynthm.common.web.core.domain;

import com.ynthm.common.web.core.enums.Gender;
import com.ynthm.common.web.core.enums.Level;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Accessors(chain = true)
@Data
public class User {
  private Gender gender;
  private Level level;
}
