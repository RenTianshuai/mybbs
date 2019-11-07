package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.Organization;
import com.yaohan.bbs.dao.mapper.OrganizationMapper;
import com.yaohan.bbs.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationMapper organizationMapper;


    @Override
    public List<Organization> findSchools() {
        return organizationMapper.findSchools();
    }

    @Override
    public List<Organization> findgrades() {
        return organizationMapper.findGrades();
    }

    @Override
    public void add(Organization organization) {
        organizationMapper.insert(organization);
    }

    @Override
    public Page<Organization> pageList(int pageNo, int pageSize) {
        Page<Organization> page = PageHelper.startPage(pageNo, pageSize);
        organizationMapper.findAll();
        return page;
    }

    @Override
    public void deleteById(String id) {
        Organization organization = organizationMapper.selectByPrimaryKey(id);
        organization.setDelFlag("1");
        organizationMapper.updateByPrimaryKey(organization);
        if (StringUtils.isEmpty(organization.getParentId())){
            organizationMapper.deleteByParentId(organization.getId());
        }
    }

    @Override
    public void modify(String id, String name) {
        Organization organization = organizationMapper.selectByPrimaryKey(id);
        organization.setName(name);
        organizationMapper.updateByPrimaryKey(organization);
    }

    @Override
    public Organization findSchoolByName(String name) {
        return organizationMapper.selectByName(name);
    }

    @Override
    public Organization findByParentIdAndName(String parentId, String name) {
        Organization organization = new Organization();
        organization.setParentId(parentId);
        organization.setName(name);
        List<Organization> list = organizationMapper.selectBy(organization);
        if (list!=null){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Organization> findByParentId(String parentId) {
        Organization organization = new Organization();
        organization.setParentId(parentId);
        List<Organization> list = organizationMapper.selectBy(organization);
        return list;
    }

    @Override
    public Organization get(String id) {
        return organizationMapper.selectByPrimaryKey(id);
    }
}
