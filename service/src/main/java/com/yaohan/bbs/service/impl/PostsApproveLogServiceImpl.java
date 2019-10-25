package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.PostsApproveLog;
import com.yaohan.bbs.dao.mapper.PostsApproveLogMapper;
import com.yaohan.bbs.service.PostsApproveLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostsApproveLogServiceImpl implements PostsApproveLogService {

    @Autowired
    PostsApproveLogMapper postsApproveLogMapper;


    @Override
    public void add(PostsApproveLog postsApproveLog) {
        postsApproveLogMapper.insert(postsApproveLog);
    }

    @Override
    public PostsApproveLog getNewByPostsId(String postsId) {
        return postsApproveLogMapper.selectByPostsId(postsId);
    }
}
