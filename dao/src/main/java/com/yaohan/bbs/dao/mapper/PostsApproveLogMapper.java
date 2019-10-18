package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.PostsApproveLog;

public interface PostsApproveLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(PostsApproveLog record);

    int insertSelective(PostsApproveLog record);

    PostsApproveLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PostsApproveLog record);

    int updateByPrimaryKey(PostsApproveLog record);
}