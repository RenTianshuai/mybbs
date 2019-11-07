package com.yaohan.bbs.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class RedisSessionDao extends AbstractSessionDAO {

    private static final String SHIRO_SESSION_PREFIX = "yh-bbs-session:";

    @Autowired
    private RedisUtil redisUtil;

    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    private void saveSession(Session session) {
        byte[] key = getKey(session.getId().toString());
        byte[] value = SerializationUtils.serialize(session);

        redisUtil.set(key, value);
        //30分钟
        redisUtil.expire(key, 1800);
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        log.debug("create session : " + sessionId.toString());
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        log.debug("read session : " + serializable.toString());
        if(serializable == null) {
            return null;
        }
        byte[] key = getKey(serializable.toString());
        byte[] value = redisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
        log.debug("update session : " + session.getId().toString());
    }

    @Override
    public void delete(Session session) {
        if(session == null || session.getId() == null){
            return;
        }
        byte[] key = getKey(session.getId().toString());
        redisUtil.del(key);
        log.debug("delete session : " + session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = redisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if(CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for(byte[] key : keys) {
            Session session = (Session) SerializationUtils.deserialize(redisUtil.get(key));
            sessions.add(session);
        }
        return sessions;
    }
}
