package com.ynthm.autoconfigure.excel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/** 单列相同内容合并策略 */
public class SingleColumnDataMergeStrategy extends AbstractMergeStrategy {

  /** 分组，每几行合并一次 */
  private final List<Integer> exportFieldGroupCountList;

  /** 目标合并列 index */
  private final Integer targetColumnIndex;

  /** 合并列数， */
  private int columnCount = 1;

  /** 需要开始合并单元格的首行index */
  private Integer rowIndex;

  public SingleColumnDataMergeStrategy(List<String> exportDataList, Integer targetColumnIndex) {
    this.exportFieldGroupCountList = getGroupCountList(exportDataList);
    this.targetColumnIndex = targetColumnIndex;
  }

  public SingleColumnDataMergeStrategy(
      List<String> exportDataList, Integer targetColumnIndex, int columnCount) {
    this.exportFieldGroupCountList = getGroupCountList(exportDataList);
    this.targetColumnIndex = targetColumnIndex;
    this.columnCount = columnCount;
  }

  @Override
  protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
    if (null == rowIndex) {
      rowIndex = cell.getRowIndex();
    }
    // 仅从首行以及目标列的单元格开始合并，忽略其他
    if (cell.getRowIndex() == rowIndex && cell.getColumnIndex() == targetColumnIndex) {
      mergeGroupColumn(sheet);
    }
  }

  private void mergeGroupColumn(Sheet sheet) {
    int rowCount = rowIndex;
    for (Integer count : exportFieldGroupCountList) {
      if (count == 1) {
        rowCount += count;
        continue;
      }
      // 合并单元格
      CellRangeAddress cellRangeAddress =
          new CellRangeAddress(
              rowCount,
              rowCount + count - 1,
              targetColumnIndex,
              targetColumnIndex + columnCount - 1);
      sheet.addMergedRegionUnsafe(cellRangeAddress);
      rowCount += count;
    }
  }

  /** 该方法将目标列根据值是否相同连续可合并，存储可合并的行数 */
  private List<Integer> getGroupCountList(List<String> exportDataList) {
    return exportDataList.stream()
        .collect(
            Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
        .values()
        .stream()
        .map(Long::intValue)
        .collect(Collectors.toList());
  }
}
