package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.Comment;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment Service接口实现.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }

    @Override
    public List<Comment> listComments(User user) {
        return commentRepository.findByUser(user);
    }

    @Override
    public Page<Comment> listUsersByContentLike(String content, Pageable pageable) {
        content = "%" + content + "%";
        return commentRepository.findByContentLike(content, pageable);
    }
}
