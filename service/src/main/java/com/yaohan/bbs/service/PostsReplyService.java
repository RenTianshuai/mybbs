package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.PostsReply;

import java.util.List;
import java.util.Map;

public interface PostsReplyService {

    Page<PostsReply> getReplyPageByPostsId(int pageNo, int pageSize, String postsId);

    PostsReply addReply(String postsId, String userId, String content);

    List<PostsReply> getRecentReplyByUserId(String userId);

    List<Map> getWeeklyTopReplys(Integer limit);

    List<Map> getWeeklyHotReplys(Integer limit);

}
