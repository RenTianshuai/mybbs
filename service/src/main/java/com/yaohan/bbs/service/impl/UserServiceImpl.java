package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.dao.mapper.UserMapper;
import com.yaohan.bbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getByUserName(String userName) {
        return userMapper.selectByUsername(userName);
    }

    @Override
    public User getByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public void add(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    public User get(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Page<User> pageAll(int pageNo, int pageSize) {
        Page<User> page = PageHelper.startPage(pageNo, pageSize);
        userMapper.findAll(new HashMap(0));
        return page;
    }

    @Override
    public Page<User> pageAll(int pageNo, int pageSize, Map params) {
        Page<User> page = PageHelper.startPage(pageNo, pageSize);
        userMapper.findAll(params);
        return page;
    }
}
