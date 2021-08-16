package com.ynthm.excel.demo.user.controller;

import com.ynthm.excel.demo.user.entity.User;
import com.ynthm.excel.demo.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Ynthm Wang
 * @version 1.0
 */
@RestController
@RequestMapping("users")
public class UserController {

  @Autowired private UserMapper userMapper;

  @PostMapping
  public void add(@RequestBody @Validated User user) {
    user.setCreateTime(LocalDateTime.now());
    userMapper.insert(user);
  }

  @GetMapping("/{id}")
  public User get(@PathVariable String id) {
    return userMapper.selectByPrimaryKey(id);
  }

  @PutMapping
  public int update(@RequestBody @Validated User user) {

    //    userMapper.updateByExampleSelective(
    //        user, new Example(User.class).createCriteria().andEqualTo("id", 1));
    return userMapper.updateByPrimaryKey(user);
  }

  @DeleteMapping("/{id}")
  public int delete(@PathVariable String id) {
    return userMapper.deleteByPrimaryKey(id);
  }
}
