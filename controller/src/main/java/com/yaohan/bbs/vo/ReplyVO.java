package com.yaohan.bbs.vo;

import com.yaohan.bbs.dao.entity.PostsReply;
import com.yaohan.bbs.dao.entity.Role;
import com.yaohan.bbs.dao.entity.User;

public class ReplyVO {

    PostsReply postsReply;

    User user;

    Role role;

    public PostsReply getPostsReply() {
        return postsReply;
    }

    public void setPostsReply(PostsReply postsReply) {
        this.postsReply = postsReply;
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
