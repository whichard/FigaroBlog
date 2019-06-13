package com.whichard.spring.boot.blog.async.handler;

import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventProducer;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeHandlerTest {

    @Autowired
    EventProducer eventProducer;

    @Test
    public void test() {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(1).setEntityId(1)
        );
    }

    @Test
    public void test1() {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(1).setEntityId(1)
        );
    }

}