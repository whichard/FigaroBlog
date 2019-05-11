package com.whichard.spring.boot.blog.repository;

import java.util.Collection;
import java.util.List;

import com.whichard.spring.boot.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User Repository 接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年4月29日
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户昵称分页查询用户列表
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户账号查询用户
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据名称列表查询用户列表
     *
     * @param usernames
     * @return
     */
    List<User> findByUsernameIn(Collection<String> usernames);
}
