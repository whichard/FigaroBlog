package com.whichard.spring.boot.blog.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.whichard.spring.boot.blog.domain.User;

/**
 * 用户服务接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月29日
 */
public interface UserService {
    /**
     * 保存头像的时候用，不会抛出异常
     *
     * @param user
     * @return
     */
    User saveOrUpdateUserWithoutException(User user);

    /**
     * 个人资料修改的时候用,会抛出邮箱占用异常
     *
     * @param user
     * @return
     */
    User saveOrUpdateUserWithProfile(User user);

    /**
     * 新增、编辑、保存用户会抛出账户名相同异常，注册和管理员修改账号用
     *
     * @param user
     * @return
     */
    User saveOrUpateUser(User user);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    User registerUser(User user);

    /**
     * 删除用户
     *
     * @param user
     * @return
     */
    void removeUser(Long id);

    /**
     * 根据id获取用户
     *
     * @param user
     * @return
     */
    User getUserById(Long id);

    /**
     * 根据用户名进行分页模糊查询
     *
     * @param user
     * @return
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);

    /**
     * 根据用户名集合，查询用户详细信息列表
     *
     * @param usernames
     * @return
     */
    List<User> listUsersByUsernames(Collection<String> usernames);
}