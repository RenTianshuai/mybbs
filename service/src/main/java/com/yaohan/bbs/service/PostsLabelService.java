package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.PostsLabel;

import java.util.List;

public interface PostsLabelService {
    List<PostsLabel>  getUsablePostsLabel();
}
