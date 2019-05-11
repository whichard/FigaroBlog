package com.whichard.spring.boot.blog.async.handler;

import com.whichard.spring.boot.blog.async.EventHandler;
import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.Message;
import com.whichard.spring.boot.blog.service.MailSender;
import com.whichard.spring.boot.blog.service.MessageService;
import com.whichard.spring.boot.blog.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wq
 * @date 2019/5/10
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String, String> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        map.put("addr","东莞");
        SendMailUtil.sendMail(model.getExt("to"), map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
