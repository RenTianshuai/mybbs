package com.yaohan.bbs.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author renti
 */
public class UserLikeLog extends UserLikeLogKey implements Serializable {
    private Boolean zan;

    private Date operaterTime;

    private String username;

    public Boolean getZan() {
        return zan;
    }

    public void setZan(Boolean zan) {
        this.zan = zan;
    }

    public Date getOperaterTime() {
        return operaterTime;
    }

    public void setOperaterTime(Date operaterTime) {
        this.operaterTime = operaterTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}