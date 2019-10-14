package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.PostsCollection;

import java.util.List;

public interface PostsCollectionService {

    PostsCollection getByUserIdAndPostsId(String userId, String postId);

    List<PostsCollection> getByUserId(String userId);

    Page<PostsCollection> pageByUserId(int pageNo, int pageSize, String userId);

    void addByUserIdAndPostsId(String userId, String postsId);

    void removeByUserIdAndPostsId(String userId, String postsId);
}
