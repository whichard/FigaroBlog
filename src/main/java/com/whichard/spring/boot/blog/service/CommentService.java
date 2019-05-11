package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.Comment;
import com.whichard.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Comment Service接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
public interface CommentService {

    /**
     * 根据id获取 Comment
     *
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 删除评论
     *
     * @param id
     * @return
     */
    void removeComment(Long id);

    /**
     * 根据用户列举评论
     *
     * @param user
     * @return
     */
    List<Comment> listComments(User user);

    /**
     * 根据评论内容进行分页模糊查询
     *
     * @param
     * @return
     */
    Page<Comment> listUsersByContentLike(String content, Pageable pageable);
}
