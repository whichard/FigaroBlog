package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.util.JedisAdapter;
import com.whichard.spring.boot.blog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author wq
 * @date 2019/4/25
 */
@Service
public class RedisCountService {
    @Autowired
    private JedisAdapter jedisAdapter;
    @Value("${blog.expiretime}")
    private int period;

    public boolean setIpCount(Long blogId, String ip) {
        String ipCountKey = RedisKeyUtil.getIpKey(blogId, ip);
        if (jedisAdapter.exist(ipCountKey)) return false;
        jedisAdapter.expireInTime(ipCountKey, period);
        return true;

    }

    public void increaseReadSize(Long blogId) {
         String readSizeKey = RedisKeyUtil.getReadSizeKey(blogId);
        jedisAdapter.incr(readSizeKey);
    }

    public long getReadSize(Long blogId) {
        String readSizeKey = RedisKeyUtil.getReadSizeKey(blogId);
        return jedisAdapter.getCount(readSizeKey);
    }

    public int getLikeCount(int entityType, long entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return (int)jedisAdapter.scard(likeKey);
    }
}
