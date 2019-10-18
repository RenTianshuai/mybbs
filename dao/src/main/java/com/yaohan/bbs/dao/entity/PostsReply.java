package com.yaohan.bbs.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PostsReply {
    private String id;

    private String userId;

    private String postsId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPostsId() {
        return postsId;
    }

    public void setPostsId(String postsId) {
        this.postsId = postsId == null ? null : postsId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}