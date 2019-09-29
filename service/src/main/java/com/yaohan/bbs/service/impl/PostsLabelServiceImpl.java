package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.mapper.PostsLabelMapper;
import com.yaohan.bbs.service.PostsLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostsLabelServiceImpl implements PostsLabelService {

    @Autowired
    PostsLabelMapper postsLebelMapper;

    @Override
    public List<PostsLabel> getUsablePostsLabel() {
        return postsLebelMapper.findActive();
    }
}
