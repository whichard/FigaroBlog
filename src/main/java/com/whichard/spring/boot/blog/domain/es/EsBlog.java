package com.whichard.spring.boot.blog.domain.es;

import java.io.Serializable;
import java.sql.Timestamp;

import com.whichard.spring.boot.blog.domain.Blog;
import com.whichard.spring.boot.blog.service.RedisCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * EsBlog 文档类.
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年3月5日
 */
@Document(indexName = "blog", type = "blog")
public class EsBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    @Autowired
    private RedisCountService redisCountService;

    @Id  // 主键
    private String id;
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long blogId; // Blog 实体的 id
    @Field(type = FieldType.String, searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String title;
    @Field(type = FieldType.String, searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String summary;
    @Field(type = FieldType.String, searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String content;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private String username;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private String avatar;
    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Timestamp createTime;
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer readSize = 0; // 访问量、阅读量
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer commentSize = 0;  // 评论量
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer voteSize = 0;  // 点赞量
    @Field(type = FieldType.String, searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String tags;  // 标签，使用IK分词，ik分词器有两个ik_max_word和ik_smart
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)  // 不做全文检索字段
    private Integer priority;

    @CompletionField(searchAnalyzer = "ik_max_word",analyzer = "ik_smart")
    private String suggest;

    protected EsBlog() {  // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String avatar, Timestamp createTime,
                  Integer readSize, Integer commentSize, Integer voteSize, String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
        this.priority = 0;
    }

    public EsBlog(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
        this.priority = blog.getPriority();

        //使用Redis数据更新计数
        //this.readSize = getRedisReadSize(blogId);
        //this.voteSize = getRedisLikeSize(blogId);
    }

    public EsBlog(Blog blog, int readSize, int voteSize) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
        this.priority = blog.getPriority();

        //使用Redis数据更新计数
        this.readSize = readSize;
        this.voteSize = voteSize;
    }

    public void update(Blog blog) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
        this.priority = blog.getPriority();
        //使用Redis数据更新计数
/*        this.readSize = getRedisReadSize(blogId);
        this.voteSize = getRedisLikeSize(blogId);*/
    }

    public void update(Blog blog, int readSize, int voteSize) {
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.summary = blog.getSummary();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
        this.tags = blog.getTags();
        this.priority = blog.getPriority();
        //使用Redis数据更新计数
        this.readSize = readSize;
        this.voteSize = voteSize;
    }


    public int getRedisReadSize(long blogId) {
        return (int)redisCountService.getReadSize(blogId);
    }

    public int getRedisLikeSize(long blogId) {
        return (int)redisCountService.getLikeCount(0, blogId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format(
                "User[blogId=%d, title='%s', summary='%s']",
                blogId, title, summary);
    }
}
