package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.PostsReply;

import java.util.List;

public interface PostsReplyMapper {
    int deleteByPrimaryKey(String id);

    int insert(PostsReply record);

    int insertSelective(PostsReply record);

    PostsReply selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PostsReply record);

    int updateByPrimaryKeyWithBLOBs(PostsReply record);

    int updateByPrimaryKey(PostsReply record);

    List<PostsReply> getByPostsId(String postsId);

    List<PostsReply> getRecentReplyByUserId(String userId);
}