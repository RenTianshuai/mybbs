package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.PostsCollection;

public interface PostsCollectionMapper {
    int deleteByPrimaryKey(String id);

    int insert(PostsCollection record);

    int insertSelective(PostsCollection record);

    PostsCollection selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PostsCollection record);

    int updateByPrimaryKey(PostsCollection record);
}