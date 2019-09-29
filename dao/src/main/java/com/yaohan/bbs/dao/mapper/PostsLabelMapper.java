package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.PostsLabel;

import java.util.List;

public interface PostsLabelMapper {
    int deleteByPrimaryKey(String id);

    int insert(PostsLabel record);

    int insertSelective(PostsLabel record);

    PostsLabel selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PostsLabel record);

    int updateByPrimaryKey(PostsLabel record);

    List<PostsLabel> findActive();
}