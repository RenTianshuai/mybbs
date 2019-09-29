package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.User;

public interface UserService {

    User getByUserName(String userName);
}
