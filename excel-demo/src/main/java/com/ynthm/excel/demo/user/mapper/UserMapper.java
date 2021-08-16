package com.ynthm.excel.demo.user.mapper;

import com.ynthm.excel.demo.user.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
public interface UserMapper extends Mapper<User> {
  /**
   * MyBatis 会自动将List与Array 参数包装在一个 Map 中
   *
   * <p>List 实例将会以 list 作为键,而数组实例将会以 array 作为键
   *
   * @param userList
   */
  void batchInsert(List<User> userList);

  @Select("SELECT * FROM t_user WHERE name=#{name}")
  List<User> queryUserByName(@Param("name") String name);

  List<User> selectUsersByIds(@Param("idList") List<Integer> idList);

  /**
   * 行行比较是 SQL-92 中引入的
   *
   * @param user
   * @return
   */
  List<User> queryByUser(User user);
}
