package com.ynthm.common.domain.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Ethan Wang
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class PageResp<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 总数 */
  private int total = 0;

  private int totalPage;

  private List<T> records = Collections.emptyList();
}
