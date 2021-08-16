package com.ynthm.excel.demo.cdc;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynthm.excel.demo.user.entity.User;
import com.ynthm.excel.demo.user.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Slf4j
@Service
public class UserCdcService implements CdcService<User> {

  private final UserMapper userMapper;

  public UserCdcService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public Class<User> getBeanClass() {
    return User.class;
  }

  @Override
  public void insert(User item) {
    log.info("insert {}", item);
  }

  @Override
  public User before(User update) {
    return userMapper.selectByPrimaryKey(update.getId());
  }

  @Override
  public void update(User update, User before) {

    try {
      User user = new User();
      setPropertyIntroSpector(User.class, before, update, user);
      user.setId(before.getId());
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      log.info("update {}", objectMapper.writeValueAsString(user));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public <T> void setPropertyIntroSpector(Class<T> clazz, T before, T after, T result)
      throws Exception {
    BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
    PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();
    if (proDescriptors != null) {
      for (PropertyDescriptor propDesc : proDescriptors) {

        Method readMethod = propDesc.getReadMethod();
        Method writeMethod = propDesc.getWriteMethod();
        Object beforeValue = readMethod.invoke(before);
        Object afterValue = readMethod.invoke(after);
        if (ObjectUtil.notEqual(beforeValue, afterValue)) {
          writeMethod.invoke(result, beforeValue);
        }
      }
    }
  }

  @Autowired private SqlSessionTemplate sqlSessionTemplate;

  @Override
  public void batchInsert(List<User> list) {

    SqlSession sqlSession =
        sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

    for (int i = 0; i < 100000; i++) {
      User user = new User();
      user.setId(i);
      user.setName("name" + i);
      userMapper.insert(user);
    }
    sqlSession.commit();
  }

  @Override
  public void delete(String id) {
    log.info("delete {}", id);
  }

  @Override
  public int updateByExampleSelective(User before, Object objExample) {

    if (objExample instanceof Example) {
      Example example = (Example) objExample;
    }

    return 0;
  }
}
