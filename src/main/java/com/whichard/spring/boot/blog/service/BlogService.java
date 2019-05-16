package com.whichard.spring.boot.blog.service;

import javax.transaction.Transactional;

import com.whichard.spring.boot.blog.domain.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.whichard.spring.boot.blog.domain.Blog;
import com.whichard.spring.boot.blog.domain.Comment;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.domain.Vote;
import com.whichard.spring.boot.blog.domain.es.EsBlog;
import com.whichard.spring.boot.blog.repository.BlogRepository;

import java.sql.Timestamp;
import java.util.*;

/**
 * Blog 服务.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年4月7日
 */
@Service
public class BlogService {

    @Autowired
    private EsBlogService esBlogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private SensitiveService sensitiveService;

    @Autowired
    private RedisCountService redisCountService;

    //博客排序分数计算
    private Blog setScore(Blog blog) {
        Date date = new Date();
        double newscore = 0.0;
        newscore = Math.log(blog.getReadSize()) * 4 + blog.getCommentSize() + blog.getVoteSize();

        long blogAge = 1, lastCommentAge = 1;
        try {
            if(blog.getCreateTime() != null)
                blogAge = (date.getTime() - blog.getCreateTime().getTime()) / (1000*3600*24);//getTime() 毫秒数 1000*3600 = 1小时
            if(blog.getLastCommentTime() != null)
                lastCommentAge = (date.getTime() - blog.getLastCommentTime().getTime()) / (1000*3600*24);
        } catch (Exception e) {

        }
        double ageScore = 0;
        ageScore = (blogAge + 1) - (lastCommentAge / 2);
        if(ageScore >= 1)
            newscore = newscore / ageScore;
        if(newscore >= 0)
            blog.setScore(newscore);
        return blog;
    }

    @Transactional
    public Blog saveBlog(Blog blog) {
        //boolean isNew1 = (blog.getId() == null);
        EsBlog esBlog = null;

        blog = setScore(blog);

        Blog returnBlog = blogRepository.save(blog);
        /*boolean isNew = (blog.getId() == null);
        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        } else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            //esBlog.setReadSize((int)redisCountService.getReadSize(blog.getId()));
            esBlog.update(returnBlog);
        }*/

        boolean isNew = (blog.getId() == null || esBlogService.getEsBlogByBlogId(blog.getId()) == null);
        if (isNew) {
            esBlog = new EsBlog(returnBlog);
        } else {
            esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
            //esBlog.setReadSize((int)redisCountService.getReadSize(blog.getId()));
            esBlog.update(returnBlog);
        }

        esBlogService.updateEsBlog(esBlog);
        return returnBlog;
    }

    @Transactional
    public void removeBlog(Long id) {
        blogRepository.delete(id);
        EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
        esBlogService.removeEsBlog(esblog.getId());
    }


    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }


    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        String tags = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByPriorityDescCreateTimeDesc(title, user, tags, user, pageable);
        return blogs;
    }


    public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }


    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReadSize(blog.getReadSize() + 1); // 在原有的阅读量基础上递增1
        this.saveBlog(blog);
    }

    public Blog createComment(Long blogId, String commentContent) {
        Blog originalBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(user, commentContent);
        originalBlog.addComment(comment);
        Date date = new Date();
        java.sql.Timestamp sq = new java.sql.Timestamp(date.getTime());
        originalBlog.setLastCommentTime(sq);
        originalBlog = setScore(originalBlog);
        return this.saveBlog(originalBlog);
    }

    public void removeComment(Long blogId, Long commentId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);
        this.saveBlog(originalBlog);
    }

    public Blog createVote(Long blogId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean isExist = originalBlog.addVote(vote);
        if (isExist) {
            throw new IllegalArgumentException("您已经点过赞了哟~");
        }
        originalBlog = setScore(originalBlog);
        return this.saveBlog(originalBlog);
    }

    public void removeVote(Long blogId, Long voteId) {
        Blog originalBlog = blogRepository.findOne(blogId);
        originalBlog.removeVote(voteId);
        this.saveBlog(originalBlog);
    }

    public Page<Blog> listHotestBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByOrderByScoreDesc(pageable);
        return blogs;
    }

    public Page<Blog> listNewestBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByOrderByCreateTimeDesc(pageable);
        return blogs;
    }

    public Page<Blog> listAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        Page<Blog> blogs = blogRepository.findByCatalog(catalog, pageable);
        return blogs;
    }

    public List<Blog> listBlogs(Catalog catalog) {
        return blogRepository.findByCatalog(catalog);
    }

    public void removeComment(Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        List<Blog> blogs = blogRepository.findByComments(comments);
        for (Blog blog : blogs) {
            this.removeComment(blog.getId(), commentId);
        }
    }

    public void removeVote(Long voteId) {
        Vote vote = voteService.getVoteById(voteId);
        List<Vote> votes = new ArrayList<>();
        votes.add(vote);
        List<Blog> blogs = blogRepository.findByVotes(votes);
        for (Blog blog : blogs) {
            this.removeVote(blog.getId(), voteId);
        }
    }

    public Page<Blog> listBlogsByTitle(String title, Pageable pageable) {
        title = "%" + title + "%";
        return blogRepository.findByTitleLike(title, pageable);
    }

    public void refreshES() {
        esBlogService.removeAllEsBlog();
        String title = "%%";
        List<Blog> blogs = blogRepository.findByTitleLike(title);
        for (Blog blog : blogs) {
            EsBlog esBlog = new EsBlog(blog);
            esBlogService.updateEsBlog(esBlog);
        }
    }
}
