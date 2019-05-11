package com.whichard.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whichard.spring.boot.blog.domain.Authority;

/**
 * Authority 仓库.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月30日
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
