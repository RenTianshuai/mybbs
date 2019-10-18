package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.dao.entity.Message;
import com.yaohan.bbs.dao.mapper.MessageMapper;
import com.yaohan.bbs.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;


    @Override
    public int countByUserId(String userId) {
        return messageMapper.countByUserId(userId);
    }

    @Override
    public List<Message> findByUserId(String userId) {
        return messageMapper.findByUserId(userId);
    }

    @Override
    public void add(Message message) {
        messageMapper.insert(message);
    }

    @Override
    public void delete(String id) {
        messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByUserId(String userId) {
        messageMapper.deleteByUserId(userId);
    }
}
