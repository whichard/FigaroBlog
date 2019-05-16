package com.whichard.spring.boot.blog.repository;

import com.whichard.spring.boot.blog.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Blog 仓库.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年6月4日
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * 根据用户名、博客标题分页查询博客列表
     *
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    /**
     * 根据用户名、博客查询博客列表（置顶和时间逆序）
     *
     * @param title
     * @param user
     * @param tags
     * @param user2
     * @param pageable
     * @return
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByPriorityDescCreateTimeDesc(String title, User user, String tags, User user2, Pageable pageable);

    /**
     * 根据分类查询博客列表
     *
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

    /**
     * 根据用户查询博客列表
     *
     * @param user
     * @return
     */
    List<Blog> findByUser(User user);

    /**
     * 根据评论查找博客，管理员删除评论和用户的时候用
     *
     * @param comments
     * @return
     */
    List<Blog> findByComments(List<Comment> comments);

    Page<Blog> findByOrderByCreateTimeDesc(Pageable pageable);

    Page<Blog> findByOrderByScoreDesc(Pageable pageable);

    /**
     * 根据点赞查找博客，管理员删除用户的时候用
     *
     * @param votes
     * @return
     */
    List<Blog> findByVotes(List<Vote> votes);

    /**
     * 根据种类查找博客，管理员删除用户和用户删除分类的时候用
     *
     * @param catalog
     * @return
     */
    List<Blog> findByCatalog(Catalog catalog);

    /**
     * 根据标题查找博客，返回分页
     *
     * @param title
     * @return
     */
    Page<Blog> findByTitleLike(String title, Pageable pageable);

    /**
     * 根据标题查找博客，返回全部
     *
     * @param title
     * @return
     */
    List<Blog> findByTitleLike(String title);
}
