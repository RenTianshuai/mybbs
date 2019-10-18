package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsername(String username);

    User selectByEmail(String email);

    List<User> findAll(Map params);
}