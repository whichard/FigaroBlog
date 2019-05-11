package com.whichard.spring.boot.blog.service;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.whichard.spring.boot.blog.config.CustomPasswordEncoder;
import com.whichard.spring.boot.blog.domain.*;
import com.whichard.spring.boot.blog.repository.BlogRepository;
import com.whichard.spring.boot.blog.repository.CommentRepository;
import com.whichard.spring.boot.blog.repository.UserRepository;
import com.whichard.spring.boot.blog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务接口实现.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月29日
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private BlogService blogService;

    @Transactional
    @Override
    public User saveOrUpdateUserWithoutException(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User saveOrUpdateUserWithProfile(User user) {
        if (user.getPassword().equals("")) {
            throw new IllegalArgumentException("密码不能为空");
        }
        //如果在查找以后对象就在缓存中，如果再进行查找，会有限找缓存中的数据
        User originalUser = userRepository.findOne(user.getId());
        User findEmailUser = userRepository.findByEmail(user.getEmail());
        originalUser.setName(user.getName());

        // 判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        PasswordEncoder encoder = new CustomPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        if (findEmailUser != null && !findEmailUser.getEmail().equals(user.getEmail()))
            throw new IllegalArgumentException("您修改的邮箱已经被占用");
        else
            originalUser.setEmail(user.getEmail());
        return userRepository.save(originalUser);
    }

    @Transactional
    @Override
    public User saveOrUpateUser(User user) {
        User findUser = userRepository.findByUsername(user.getUsername());
        if (findUser != null && findUser.getId() != user.getId()) {
            throw new IllegalArgumentException("该账号名已经被占用");
        }
        findUser = userRepository.findByEmail(user.getEmail());
        if (findUser != null && findUser.getId() != user.getId()) {
            System.out.println(findUser);
            throw new IllegalArgumentException("该邮箱已经被占用");
        }
        User newOne = new User(null, null, null, null);
        newOne.setEncodePassword("");
        if (newOne.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        User findUser = userRepository.findByUsername(user.getUsername());
        if (findUser != null) {
            throw new IllegalArgumentException("该账号名已经被占用");
        }
        findUser = userRepository.findByEmail(user.getEmail());
        if (findUser != null) {
            throw new IllegalArgumentException("该邮箱已经被占用");
        }
        User newOne = new User(null, null, null, null);
        newOne.setEncodePassword("");
        if (newOne.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        User user = userRepository.findOne(id);
        //先删除用户的点赞记录
        List<Vote> votes = voteService.listVote(user);
        for (Vote vote : votes) {
            blogService.removeVote(vote.getId());
            voteService.removeVote(vote.getId());
        }
        //然后删除用户的评论
        List<Comment> comments = commentService.listComments(user);
        for (Comment comment : comments) {
            blogService.removeComment(comment.getId());
            commentService.removeComment(comment.getId());
        }
        //然后删除用户的种类(同时也会删除用户的所有文章,如果是博主的话)
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        for (Catalog catalog : catalogs) {
            catalogService.removeCatalog(catalog.getId());
        }
        userRepository.delete(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        // 模糊查询
        name = "%" + name + "%";
        Page<User> users = userRepository.findByNameLike(name, pageable);
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " 不存在");
        }
        return user;
    }

    @Override
    public List<User> listUsersByUsernames(Collection<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }
}
