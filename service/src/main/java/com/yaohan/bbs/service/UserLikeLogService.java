package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.UserLikeLog;

import java.util.List;

public interface UserLikeLogService {

    UserLikeLog get(String userId, String postsId);


    UserLikeLog zan(UserLikeLog userLikeLog);

    UserLikeLog cancel(UserLikeLog userLikeLog);

    List<UserLikeLog> findZanUsers(String postsId);
}
