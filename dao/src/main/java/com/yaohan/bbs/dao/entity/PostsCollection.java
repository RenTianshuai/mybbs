package com.yaohan.bbs.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author renti
 */
public class PostsCollection implements Serializable {
    private String id;

    private String userId;

    private String postsId;

    private Date createTime;

    private String postsName;

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

    public String getPostsName() {
        return postsName;
    }

    public void setPostsName(String postsName) {
        this.postsName = postsName;
    }
}