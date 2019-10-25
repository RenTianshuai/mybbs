package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.Organization;
import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.entity.User;
import com.yaohan.bbs.dao.mapper.UserMapper;
import com.yaohan.bbs.excel.UserModel;
import com.yaohan.bbs.service.OrganizationService;
import com.yaohan.bbs.service.RoleService;
import com.yaohan.bbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    RoleService roleService;

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

    @Override
    public List<User> getBy(Map params) {
        return userMapper.findAll(params);
    }

    @Override
    public User getByAuthInfo(String authInfo) {
        return userMapper.selectByAuthInfo(authInfo);
    }

    @Override
    public User getByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    @Override
    public void updateOrganizationImport(UserModel userModel) throws Exception{
        User user = getByUserName(userModel.getUsername());
        if (user == null){
            throw new Exception("没有用户："+userModel.getUsername());
        }
        Organization organizationSchool = organizationService.findSchoolByName(userModel.getSchool());
        if (organizationSchool == null){
            throw new Exception("错误学校名："+userModel.getSchool());
        }
        Organization organizationClass = organizationService.findByParentIdAndName(organizationSchool.getId(), userModel.getClassName());
        if (organizationClass == null){
            throw new Exception("错误班级名："+userModel.getClassName());
        }
        Role role = roleService.get(userModel.getRoleId());
        if (role == null){
            throw new Exception("错误角色ID："+userModel.getRoleId());
        }
        user.setRoleId(role.getId());
        user.setSchool(organizationSchool.getId());
        user.setClassName(organizationClass.getId());
        user.setRealname(userModel.getRealname());
        this.update(user);
    }
}
