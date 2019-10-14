package com.yaohan.bbs.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsReply;
import com.yaohan.bbs.dao.mapper.PostsMapper;
import com.yaohan.bbs.dao.mapper.PostsReplyMapper;
import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.service.PostsReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostsReplyServiceImpl implements PostsReplyService {

    @Autowired
    PostsReplyMapper postsReplyMapper;
    @Autowired
    FlowNoService flowNoService;
    @Autowired
    PostsMapper postsMapper;


    @Override
    public Page<PostsReply> getReplyPageByPostsId(int pageNo, int pageSize, String postsId) {
        Page<PostsReply> page = PageHelper.startPage(pageNo, pageSize);
        postsReplyMapper.getByPostsId(postsId);
        return page;
    }

    @Override
    public void addReply(String postsId, String userId, String content) {
        PostsReply postsReply = new PostsReply();
        postsReply.setId(flowNoService.generateFlowNo());
        postsReply.setPostsId(postsId);
        postsReply.setUserId(userId);
        postsReply.setContent(content);
        postsReply.setCreateTime(new Date());
        postsReplyMapper.insert(postsReply);

        //更新评论数
        Posts posts = postsMapper.selectByPrimaryKeyWithoutBLOBS(postsId);
        posts.setReadCount(posts.getReadCount() + 1);
        postsMapper.updateByPrimaryKeySelective(posts);
    }

    @Override
    public List<PostsReply> getRecentReplyByUserId(String userId) {
        return postsReplyMapper.getRecentReplyByUserId(userId);
    }
}
