package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.UserLikeLog;
import com.yaohan.bbs.dao.entity.UserLikeLogKey;
import com.yaohan.bbs.dao.mapper.PostsMapper;
import com.yaohan.bbs.dao.mapper.UserLikeLogMapper;
import com.yaohan.bbs.service.UserLikeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class UserLikeLogServiceImpl implements UserLikeLogService {

    @Autowired
    UserLikeLogMapper userLikeLogMapper;

    @Autowired
    PostsMapper postsMapper;

    @Override
    public UserLikeLog get(String userId, String postsId) {
        UserLikeLogKey key = new UserLikeLogKey();
        key.setUserId(userId);
        key.setPostsId(postsId);
        UserLikeLog log = userLikeLogMapper.selectByPrimaryKey(key);
        if (log == null){
            log = initUserLikeLog(userId, postsId);
            userLikeLogMapper.insert(log);
        }
        return log;
    }

    @Override
    @Transactional
    public UserLikeLog zan(UserLikeLog userLikeLog) {
        userLikeLog.setZan(true);
        userLikeLog.setOperaterTime(new Date());
        userLikeLogMapper.updateByPrimaryKey(userLikeLog);
        postsMapper.addExperience(userLikeLog.getPostsId());
        return userLikeLog;
    }

    @Override
    @Transactional
    public UserLikeLog cancel(UserLikeLog userLikeLog) {
        userLikeLog.setZan(false);
        userLikeLog.setOperaterTime(new Date());
        userLikeLogMapper.updateByPrimaryKey(userLikeLog);
        postsMapper.subExperience(userLikeLog.getPostsId());
        return userLikeLog;
    }


    private UserLikeLog initUserLikeLog(String userId, String postsId){
        UserLikeLog log = new UserLikeLog();
        log.setUserId(userId);
        log.setPostsId(postsId);
        log.setZan(false);
        log.setOperaterTime(new Date());
        return log;
    }
}
