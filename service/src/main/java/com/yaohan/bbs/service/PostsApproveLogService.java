package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.PostsApproveLog;

public interface PostsApproveLogService {

    void add(PostsApproveLog postsApproveLog);

    PostsApproveLog getNewByPostsId(String postsId);
}
