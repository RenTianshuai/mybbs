package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.dao.mapper.UserMapper;
import com.yaohan.bbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}