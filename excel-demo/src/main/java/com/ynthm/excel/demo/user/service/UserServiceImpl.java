package com.ynthm.excel.demo.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ynthm.excel.demo.user.entity.User;
import com.ynthm.excel.demo.user.mapper.UserMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired private SqlSessionTemplate sqlSessionTemplate;

  @Autowired private UserMapper userMapper;

  @Override
  public void saveBatchExecutorType(List<User> userList) {
    SqlSession sqlSession =
        sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

    for (User user : userList) {
      userMapper.insert(user);
    }

    sqlSession.commit();
  }

  @Override
  public PageInfo<User> queryPage(Integer pageNo, Integer pageSize) {
    PageHelper.startPage(pageNo, pageSize);
    List<User> list = userMapper.selectAll();
    return new PageInfo<>(list);
  }
}
