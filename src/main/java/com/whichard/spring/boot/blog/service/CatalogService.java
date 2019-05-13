package com.whichard.spring.boot.blog.service;

import java.util.List;

import com.whichard.spring.boot.blog.domain.Blog;
import com.whichard.spring.boot.blog.domain.Catalog;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.repository.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Catalog 服务接口实现.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月7日
 */
@Service
public class CatalogService{
    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private BlogService blogService;

    public Catalog saveCatalog(Catalog catalog) {
        // 判断重复
        List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
        if (list != null && list.size() > 0) {
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
    }

    public void removeCatalog(Long id) {
        Catalog catalog = getCatalogById(id);
        List<Blog> blogs = blogService.listBlogs(catalog);
        for (Blog blog : blogs) {
            blogService.removeBlog(blog.getId());
        }
        catalogRepository.delete(id);
    }

    public Catalog getCatalogById(Long id) {
        return catalogRepository.findOne(id);
    }

    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }

}
