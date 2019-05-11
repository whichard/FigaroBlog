package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.Authority;

/**
 * Authority 服务接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月30日
 */
public interface AuthorityService {
    /**
     * 根据ID查询 Authority
     *
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
