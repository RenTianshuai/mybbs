package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.excel.UserModel;

import java.util.List;
import java.util.Map;

public interface UserService {

    User getByUserName(String userName);

    User getByEmail(String email);

    void add(User user);

    User get(String userId);

    void update(User user);

    Page<User> pageAll(int pageNo, int pageSize);

    Page<User> pageAll(int pageNo, int pageSize, Map params);

    List<User> getBy(Map params);

    User getByAuthInfo(String authInfo);

    User getByPhone(String phone);

    void updateOrganizationImport(UserModel userModel) throws Exception;
}
