package com.yaohan.bbs.service;

import com.github.pagehelper.Page;
import com.yaohan.bbs.dao.entity.Posts;

import java.util.List;
import java.util.Map;

public interface PostsServcie {

    void add(Posts posts);

    Posts get(String id);

    Posts getWithoutBLOBS(String id);

    List<Posts> allPublishPosts();

    Page<Posts> allPublishPostsByPage(int pageNo, int pageSize, Map params);

    List<Posts> topPublishPostsByNum(int num);

    Page<Posts> findPostsByPage(int pageNo, int pageSize, Map params);

    void update(Posts posts);

    Page<Posts> allPublishPostsByPageAndReplys(int pageNo, int pageSize, Map params);
}
