package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.Organization;

import java.util.List;

public interface OrganizationService {

    List<Organization> findSchools();

    List<Organization> findgrades();

    void add(Organization organization);

    Page<Organization> pageList(int pageNo, int pageSize);

    void deleteById(String id);

    void modify(String id, String name);

    Organization findSchoolByName(String name);

    Organization findByParentIdAndName(String parentId, String name);

    Organization get(String id);

    List<Organization> findByParentId(String parentId);
}
