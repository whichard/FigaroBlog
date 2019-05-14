package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @Test
    public void addMessage() {
        for (int j = 0; j < 10; j++) {
            for (int i = 1; i <= 10; i++) {
                Message message = new Message();
                message.setContent("hello" + i + "from" + (j));
                message.setFromId(i);
                message.setToId(1);
                message.setHasRead(0);
                messageService.addMessage(message);
            }

        }
    }

  /*  @Test
    public void getConversationList() {
        List<Message> list = messageService.getConversationList(1);
        for(Message m : list)
            System.out.println(m.toString());
    }

    @Test
    public void getConversionDetail() {
        List<Message> list = messageService.getConversationList(1);
        for(Message m : list) {
            String cId = m.getConversationId();
            List<Message> conv = messageService.getConversionDetail(cId);
            for(Message m1 : conv)
                System.out.println(m.getFromId() +"" + m.getToId());
        }

    }*/

    /*@Test
    public void getConversationUnreadCount() {
        int count = messageService.getConversationUnreadCount(1, "1_2");
        System.out.println(count);
    }*/

}