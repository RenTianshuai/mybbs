package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.Posts;

public interface PostsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Posts record);

    int insertSelective(Posts record);

    Posts selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Posts record);

    int updateByPrimaryKeyWithBLOBs(Posts record);

    int updateByPrimaryKey(Posts record);
}