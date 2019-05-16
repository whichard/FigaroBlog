package com.whichard.spring.boot.blog.domain;

import com.whichard.spring.boot.blog.service.BlogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author wq
 * @date 2019/5/16
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BlogTest {

    @Autowired
    BlogService blogService;

    @Test
    public void setScore() {
        for (int i = 5; i <= 14; i++) {
            Blog blog = new Blog();
            if(blogService.getBlogById((long)i) == null)
                continue;
            blog =blogService.getBlogById((long)i);
            blogService.saveBlog(blog);
        }
    }
}