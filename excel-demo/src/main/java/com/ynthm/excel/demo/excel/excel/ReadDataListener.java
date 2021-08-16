package com.ynthm.excel.demo.excel.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 模板的读取类
 *
 * @author Ethan Wang
 */
@Slf4j
public class ReadDataListener<T> extends AnalysisEventListener<T> {
  /** 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收 */
  private static final int BATCH_COUNT = 5;

  List<T> list = new ArrayList<>();
  /** 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。 */
  private final Consumer<List<T>> consumer;

  public ReadDataListener(Consumer<List<T>> consumer) {
    this.consumer = consumer;
  }

  /**
   * 这个每一条数据解析都会来调用
   *
   * @param data one row value. Is is same as {@link AnalysisContext#readRowHolder()}
   * @param context
   */
  @Override
  public void invoke(T data, AnalysisContext context) {
    list.add(data);
    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
    if (list.size() >= BATCH_COUNT) {
      saveData();
      // 存储完成清理 list
      list.clear();
    }
  }

  /**
   * 所有数据解析完成了 都会来调用
   *
   * @param context
   */
  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("所有数据解析完成！");
  }

  @Override
  public void onException(Exception exception, AnalysisContext context) {
    log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
    // 如果是某一个单元格的转换异常 能获取到具体行号
    // 如果要获取头的信息 配合invokeHeadMap使用
    if (exception instanceof ExcelDataConvertException) {
      ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
      log.error(
          "第{}行，第{}列解析异常，数据为:{}",
          excelDataConvertException.getRowIndex(),
          excelDataConvertException.getColumnIndex(),
          excelDataConvertException.getCellData());
    }
  }

  /** 加上存储数据库 */
  private void saveData() {
    if (!list.isEmpty()) {
      log.info("{}条数据，开始存储数据库！", list.size());
      consumer.accept(list);
      log.info("存储数据库成功！");
    }
  }
}
