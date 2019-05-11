package com.whichard.spring.boot.blog.repository;

import java.util.List;

import com.whichard.spring.boot.blog.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whichard.spring.boot.blog.domain.User;

/**
 * Catalog Repository.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月7日
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    /**
     * 根据用户查询
     *
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户、分类名称查询
     *
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user, String name);
}
