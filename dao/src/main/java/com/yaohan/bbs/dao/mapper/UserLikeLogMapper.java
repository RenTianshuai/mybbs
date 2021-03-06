package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.UserLikeLog;
import com.yaohan.bbs.dao.entity.UserLikeLogKey;

import java.util.List;

public interface UserLikeLogMapper {
    int deleteByPrimaryKey(UserLikeLogKey key);

    int insert(UserLikeLog record);

    int insertSelective(UserLikeLog record);

    UserLikeLog selectByPrimaryKey(UserLikeLogKey key);

    int updateByPrimaryKeySelective(UserLikeLog record);

    int updateByPrimaryKey(UserLikeLog record);

    List<UserLikeLog> findAllZanUsersByPostsId(String postsId);
}