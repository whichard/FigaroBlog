package com.whichard.spring.boot.blog.repository;

import com.whichard.spring.boot.blog.domain.Comment;
import com.whichard.spring.boot.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whichard.spring.boot.blog.domain.Vote;

import java.util.List;

/**
 * Vote Repository接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
    /**
     * 根据用户查询
     *
     * @param user
     * @return
     */
    List<Vote> findByUser(User user);
}
