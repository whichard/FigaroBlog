package com.whichard.spring.boot.blog.async;

import com.alibaba.fastjson.JSONObject;
import com.whichard.spring.boot.blog.util.JedisAdapter;
import com.whichard.spring.boot.blog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wq
 * @date 2019/5/10
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            System.out.println("hi");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
