package com.whichard.spring.boot.blog.vo;

/**
 * Created by Whichard on 2018/3/22.
 */
public class BlogIPVO {
    private Long blogId;
    private String IP;

    public BlogIPVO(Long bolgId, String IP) {
        this.blogId = bolgId;
        this.IP = IP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogIPVO other = (BlogIPVO) o;

        if (blogId != other.getBlogId()) return false;
        if (!IP.equals(other.getIP())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = blogId.hashCode();
        result = result + IP.hashCode();
        return result;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
