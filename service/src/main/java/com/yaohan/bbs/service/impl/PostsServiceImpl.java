package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.mapper.PostsMapper;
import com.yaohan.bbs.service.PostsServcie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PostsServiceImpl implements PostsServcie {

    @Autowired
    PostsMapper postsMapper;

    @Override
    public void add(Posts posts) {
        postsMapper.insertSelective(posts);
    }

    @Override
    public Posts get(String id) {
        return postsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Posts getWithoutBLOBS(String id) {
        return postsMapper.selectByPrimaryKeyWithoutBLOBS(id);
    }

    @Override
    public List<Posts> allPublishPosts() {
        return postsMapper.findAllPubishPosts();
    }

    @Override
    public Page<Posts> allPublishPostsByPage(int pageNo, int pageSize, Map params) {
        Page<Posts> page = PageHelper.startPage(pageNo, pageSize);
        postsMapper.findAllPubishPostsBy(params);
        return page;
    }

    @Override
    public List<Posts> topPublishPostsByNum(int num) {
        return postsMapper.topPublishPostsByNum(num);
    }

    @Override
    public Page<Posts> findostsByPage(int pageNo, int pageSize, Map params) {
        Page<Posts> page = PageHelper.startPage(pageNo, pageSize);
        postsMapper.findPostsBy(params);
        return page;
    }
}
