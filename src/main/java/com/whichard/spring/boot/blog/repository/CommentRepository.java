package com.whichard.spring.boot.blog.repository;

import com.whichard.spring.boot.blog.domain.Comment;
import com.whichard.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Comment Repository 接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 根据用户查询
     *
     * @param user
     * @return
     */
    List<Comment> findByUser(User user);

    /**
     * 根据内容模糊查询
     *
     * @param content
     * @param pageable
     * @return
     */
    Page<Comment> findByContentLike(String content, Pageable pageable);
}
