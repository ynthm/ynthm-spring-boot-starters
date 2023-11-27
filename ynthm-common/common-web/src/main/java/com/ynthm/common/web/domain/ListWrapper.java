package com.ynthm.common.web.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class ListWrapper<E> {
  @NotEmpty @Valid private List<E> list;

  public ListWrapper() {
    this.list = new ArrayList<>();
  }

  public ListWrapper(List<E> list) {
    this.list = list;
  }
}
