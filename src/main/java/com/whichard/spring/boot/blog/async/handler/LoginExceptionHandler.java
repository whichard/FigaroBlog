package com.whichard.spring.boot.blog.async.handler;

import com.whichard.spring.boot.blog.async.EventHandler;
import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.Message;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.service.MailSender;
import com.whichard.spring.boot.blog.service.MessageService;
import com.whichard.spring.boot.blog.service.UserService;
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
    UserService userService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String, String> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        map.put("addr", model.getExt("addr"));
        SendMailUtil.sendMail(model.getExt("to"), map);

        Message message = new Message();
        User user = userService.getUserById((long)model.getActorId());
        int toId = 1, fromId = 999; //系统id999
        try {
            toId = user.getId().intValue();
            //if(fromId == toId) return;
            //被赞blog作者的ID
            message.setToId(toId);
            message.setContent("用户" + user.getUsername() + "你好，" +
                    "你的账号存在异地登录， 登录地点为：" + model.getExt("addr"));
            // SYSTEM ACCOUNT
            //message.setFromId(3);
            //message.setFromId(fromId);

            message.setCreatedDate(new Date());
            messageService.addMessage(message);
        } catch (Exception e) {
            System.out.println("Error likeHandler : " + e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
