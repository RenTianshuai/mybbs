package com.yaohan.bbs.dao.mapper;

import com.yaohan.bbs.dao.entity.Message;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);

    int countByUserId(String userId);

    List<Message> findByUserId(String userId);

    int deleteByUserId(String id);
}