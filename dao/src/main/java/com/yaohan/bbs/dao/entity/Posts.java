package com.yaohan.bbs.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.text.Format;
import java.util.Date;

/**
 * @author renti
 */
public class Posts implements Serializable {
    private String id;

    private String labelId;

    private String title;

    private Integer experience;

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date publishTime;

    private Integer readCount;

    private Boolean top;

    private Boolean essence;

    private Byte status;

    private Byte score;

    private String approver;

    private String delFlag;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId == null ? null : labelId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Boolean getTop() {
        return top;
    }

    public void setTop(Boolean top) {
        this.top = top;
    }

    public Boolean getEssence() {
        return essence;
    }

    public void setEssence(Boolean essence) {
        this.essence = essence;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
        this.score = score;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver == null ? null : approver.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    //非库字段，用于传所需信息

    private String labelName;

    private String realName;

    private String userName;

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}