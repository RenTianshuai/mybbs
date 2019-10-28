package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.UserLikeLog;

public interface UserLikeLogService {

    UserLikeLog get(String userId, String postsId);


    UserLikeLog zan(UserLikeLog userLikeLog);

    UserLikeLog cancel(UserLikeLog userLikeLog);
}
