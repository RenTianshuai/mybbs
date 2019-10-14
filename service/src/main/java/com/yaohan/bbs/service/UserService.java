package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.User;

public interface UserService {

    User getByUserName(String userName);

    User getByEmail(String email);

    void add(User user);

    User get(String userId);

    void update(User user);
}
