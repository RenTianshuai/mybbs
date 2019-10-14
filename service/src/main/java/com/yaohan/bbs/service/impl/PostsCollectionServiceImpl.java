package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.PostsCollection;
import com.yaohan.bbs.dao.mapper.PostsCollectionMapper;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.PostsCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PostsCollectionServiceImpl implements PostsCollectionService {

    @Autowired
    PostsCollectionMapper postsCollectionMapper;
    @Autowired
    FlowNoService flowNoService;

    @Override
    public List<PostsCollection> getByUserId(String userId) {
        Map params = new HashMap(1);
        params.put("userId", userId);
        return postsCollectionMapper.selectBy(params);
    }

    @Override
    public Page<PostsCollection> pageByUserId(int pageNo, int pageSize, String userId) {
        Page<PostsCollection> page = PageHelper.startPage(pageNo, pageSize);
        postsCollectionMapper.selectWithPostsNameByUserId(userId);
        return page;
    }

    @Override
    public PostsCollection getByUserIdAndPostsId(String userId, String postId) {
        Map params = new HashMap(2);
        params.put("userId", userId);
        params.put("postsId", postId);
        List<PostsCollection> list = postsCollectionMapper.selectBy(params);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void addByUserIdAndPostsId(String userId, String postsId) {
        PostsCollection postsCollection = new PostsCollection();
        postsCollection.setId(flowNoService.generateFlowNo());
        postsCollection.setUserId(userId);
        postsCollection.setPostsId(postsId);
        postsCollection.setCreateTime(new Date());
        postsCollectionMapper.insert(postsCollection);
    }

    @Override
    public void removeByUserIdAndPostsId(String userId, String postsId) {
        postsCollectionMapper.deleteByPrimaryKey(getByUserIdAndPostsId(userId, postsId).getId());
    }
}
