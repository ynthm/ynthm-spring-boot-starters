package com.ynthm.common.utils;

import com.ynthm.common.bean.OrderDto;
import com.ynthm.common.bean.OrderEntity;
import net.sf.cglib.beans.BeanCopier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
class BeanCopierUtilTest {

  @Test
  public void normalCopyTest() {
    OrderEntity entity = new OrderEntity();
    entity.setId(1);
    entity.setName("orderName");
    final BeanCopier copier = BeanCopier.create(OrderEntity.class, OrderDto.class, false);
    OrderDto dto = new OrderDto();
    copier.copy(entity, dto, null);
    Assertions.assertEquals(1, dto.getId());
    Assertions.assertEquals("orderName", dto.getName());
  }

  @Test
  public void copyWithConverterTest() {
    OrderEntity entity = new OrderEntity();
    entity.setId(1);
    entity.setName("orderName");
    entity.setAmount(new BigDecimal("100"));
    entity.setCreateTime(LocalDateTime.now());
    final BeanCopier copier = BeanCopier.create(OrderEntity.class, OrderDto.class, true);
    OrderDto dto = new OrderDto();

    copier.copy(
        entity,
        dto,
        (s, targetClass, t) -> {
          if (s instanceof BigDecimal && targetClass == String.class) {
            return s.toString();
          } else if (s instanceof LocalDateTime) {
            return ((LocalDateTime) s).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
          } else if ("setId".equals(t)) {
            return s;
          }
          return s;
        });

    Assertions.assertEquals(1, dto.getId());
    Assertions.assertEquals("orderName", dto.getName());
  }
}
