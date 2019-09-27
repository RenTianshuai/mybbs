package com.yaohan.bbs.shiro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

@Component
public class RedisUtil {

    @Autowired
    JedisPool jedisPool;

    public void set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        } finally{
            jedis.close();
        }
    }

    public void expire(byte[] key, int time) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire(key, time);
        } finally{
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } finally{
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(key);
        } finally{
            jedis.close();
        }
    }

    public Set<byte[]> keys(String shiroRedisPrefis) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.keys((shiroRedisPrefis + "*").getBytes());
        } finally{
            jedis.close();
        }
    }
}
