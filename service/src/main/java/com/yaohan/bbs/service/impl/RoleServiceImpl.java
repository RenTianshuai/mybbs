package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.mapper.RoleMapper;
import com.yaohan.bbs.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public Role get(String roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }
}
