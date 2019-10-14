package com.yaohan.bbs.vo;

import com.yaohan.bbs.dao.entity.Posts;
import com.yaohan.bbs.dao.entity.PostsLabel;
import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.entity.User;

public class PostsVO {

    PostsLabel label;

    Posts posts;

    User user;

    Role role;

    public PostsLabel getLabel() {
        return label;
    }

    public void setLabel(PostsLabel label) {
        this.label = label;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
