package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.domain.Vote;

import java.util.List;

/**
 * Vote 服务接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年4月9日
 */
public interface VoteService {
    /**
     * 根据id获取 Vote
     *
     * @param id
     * @return
     */
    Vote getVoteById(Long id);

    /**
     * 删除Vote
     *
     * @param id
     * @return
     */
    void removeVote(Long id);

    /**
     * 根据用户列举点赞
     *
     * @param user
     * @return
     */
    List<Vote> listVote(User user);
}
