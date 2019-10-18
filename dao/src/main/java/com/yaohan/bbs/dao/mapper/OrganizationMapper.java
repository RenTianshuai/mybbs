package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.Organization;

import java.util.List;

public interface OrganizationMapper {
    int deleteByPrimaryKey(String id);

    int insert(Organization record);

    int insertSelective(Organization record);

    Organization selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);

    List<Organization> findSchools();

    List<Organization> findAll();

    int deleteByParentId(String parentId);

    Organization selectByName(String name);

    List<Organization> selectBy(Organization record);
}