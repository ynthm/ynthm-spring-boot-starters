package com.ynthm.common.domain.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Ethan Wang
 */
@Accessors(chain = true)
@Data
public class PageReq<P> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 查询參數 可以为 Void */
  protected P param;

  /** 每页显示条数，默认 10 */
  protected int size = 10;

  /** 当前页 */
  protected int page = 1;

  /** 排序字段信息 */
  private List<OrderItem> orderItems = new ArrayList<>();

  private boolean searchCount = true;

  public PageReq() {
    // 空构造器
  }

  /** --------------- 以下为静态构造方式 --------------- */
  public static <P extends Serializable> PageReq<P> of(int page, int size) {
    return new PageReq<P>().setPage(page).setSize(size);
  }

  /**
   * 分页参数为空时实例化 方便 XxxMapper.xml 不用判断 param 为 null
   *
   * @param supplier P::new
   */
  public P getParam(Supplier<P> supplier) {
    if (param == null) {
      param = supplier.get();
      return param;
    }

    return param;
  }

  public Optional<P> optParam() {
    return Optional.ofNullable(param);
  }
}
