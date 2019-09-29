package com.yaohan.bbs.controller;

import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.service.PostsLabelService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseController {

    @Autowired
    PostsLabelService postsLabelService;

    List<PostsLabel> getPostsLabel(){
        return postsLabelService.getUsablePostsLabel();
    }
}
