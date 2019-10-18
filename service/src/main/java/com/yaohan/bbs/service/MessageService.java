package com.yaohan.bbs.service;

import com.yaohan.bbs.dao.entity.Message;

import java.util.List;

public interface MessageService {

    int countByUserId(String userId);

    List<Message> findByUserId(String userId);

    void add(Message message);

    void delete(String id);

    void deleteByUserId(String userId);
}
