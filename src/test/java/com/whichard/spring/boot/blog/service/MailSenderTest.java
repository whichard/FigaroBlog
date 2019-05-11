package com.whichard.spring.boot.blog.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSenderTest {

    @Autowired
    MailSender mailSender;

    @Test
    public void sendWithHTMLTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", null);
        mailSender.sendWithHTMLTemplate("whichard@qq.com", "Hello", "mails/welcome.html", map);
    }
}