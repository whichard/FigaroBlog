package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whichard.spring.boot.blog.domain.Vote;
import com.whichard.spring.boot.blog.repository.VoteRepository;

import java.util.List;

/**
 * Vote 服务实现.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月6日
 */
@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;


    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }


    public void removeVote(Long id) {
        voteRepository.delete(id);
    }


    public List<Vote> listVote(User user) {
        return voteRepository.findByUser(user);
    }
}
