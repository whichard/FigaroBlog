package com.whichard.spring.boot.blog.service;

import java.util.List;

import com.whichard.spring.boot.blog.domain.Catalog;
import com.whichard.spring.boot.blog.domain.User;

/**
 * Catalog 服务接口.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年4月10日
 */
public interface CatalogService {
    /**
     * 保存Catalog
     *
     * @param catalog
     * @return
     */
    Catalog saveCatalog(Catalog catalog);

    /**
     * 删除Catalog
     *
     * @param id
     * @return
     */
    void removeCatalog(Long id);

    /**
     * 根据id获取Catalog
     *
     * @param id
     * @return
     */
    Catalog getCatalogById(Long id);

    /**
     * 获取Catalog列表
     *
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
