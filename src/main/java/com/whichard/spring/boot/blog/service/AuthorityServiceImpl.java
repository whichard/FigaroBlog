/**
 *
 */
package com.whichard.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whichard.spring.boot.blog.domain.Authority;
import com.whichard.spring.boot.blog.repository.AuthorityRepository;

/**
 * Authority 服务接口的实现.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月30日
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    /* (non-Javadoc)
     * @see AuthorityService#getAuthorityById(java.lang.Long)
     */
    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }

}
