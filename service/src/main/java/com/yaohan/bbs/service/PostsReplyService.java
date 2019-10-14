package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.PostsReply;

import java.util.List;

public interface PostsReplyService {

    Page<PostsReply> getReplyPageByPostsId(int pageNo, int pageSize, String postsId);

    void addReply(String postsId, String userId, String content);

    List<PostsReply> getRecentReplyByUserId(String userId);
}
