package com.ynthm.common.excel.domain;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Ethan Wang
 * @version 1.0
 */
@Data
public class IgnoreData {
  @ExcelIgnore
  private String ignore;
  
  @ExcelProperty(value = "创建时间")
  @ColumnWidth(25)
  private LocalDateTime createTime;
}
