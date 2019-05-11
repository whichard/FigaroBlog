package com.whichard.spring.boot.blog.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMailUtilTest {

    @Test
    public void sendMail() {
            Map<String, String> map = new HashMap<>();
            map.put("username", "王尼玛");
            map.put("addr","东莞");
            SendMailUtil.sendMail("whichard@qq.com", map);
    }
}