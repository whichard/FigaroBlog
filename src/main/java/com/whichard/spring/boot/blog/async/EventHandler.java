package com.whichard.spring.boot.blog.async;
import java.util.List;
/**
 * @author wq
 * @date 2019/5/10
 */

public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}

