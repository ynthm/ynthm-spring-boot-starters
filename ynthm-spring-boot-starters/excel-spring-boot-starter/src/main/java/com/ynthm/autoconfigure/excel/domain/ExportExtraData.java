package com.ynthm.autoconfigure.excel.domain;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@SuperBuilder
@Data
public class ExportExtraData<T> extends ExcelExtraData<T> {

  private int total;
}
