package com.ynthm.demo.mybatis;

import com.ynthm.demo.mybatis.user.entity.Role;
import com.ynthm.demo.mybatis.user.mapper.RoleMapper;
import com.ynthm.demo.mybatis.user.service.TestService;
import com.ynthm.demo.mybatis.user.service.impl.RoleServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@SpringBootTest
public class MockTest {
  private AutoCloseable closeable;

  @Mock RoleMapper roleMapper;

  @InjectMocks RoleServiceImpl roleService;

  @Autowired TestService testService;

  @Test
  public void whenUseInjectMocksAnnotation_thenCorrect() {
    Role role = new Role();

    final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

    Mockito.when(roleMapper.selectById(1L)).thenReturn(role);
    assertEquals(role, testService.getOne(1L));

    // 验证参数
    Mockito.verify(roleMapper).selectById(captor.capture());
    assertEquals(1L, captor.getValue());
  }

  @BeforeEach
  void beforeEach() {

    ReflectionTestUtils.setField(testService, "roleService", roleService);
  }

  @Before
  public void openMocks() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @After
  public void releaseMocks() throws Exception {
    closeable.close();
  }
}
