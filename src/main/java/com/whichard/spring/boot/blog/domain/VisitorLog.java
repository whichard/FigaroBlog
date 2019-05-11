package com.whichard.spring.boot.blog.domain;

import com.whichard.spring.boot.blog.util.IpUtil;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Whichard on 2018/4/15.
 */
@Entity
public class VisitorLog {
    private static final long serialVersionUID = 1L;

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id; // 用户的唯一标识


    @NotEmpty(message = "访问地址不能为空")
    @Column(nullable = false)
    private String url;

    @NotEmpty(message = "访问方法不能为空")
    @Column(nullable = false)
    private String httpMethod;

    @NotEmpty(message = "IP地址不能为空")
    @Column(nullable = false)
    private String ip;


    @NotEmpty(message = "IP地址映射的真实不能为空")
    @Column(nullable = false)
    private String trueAddress;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Timestamp visitTime;


    @NotEmpty(message = "后台访问方法不能为空")
    @Column(nullable = false)
    private String classMethod;

    @NotEmpty(message = "访问方法参数")
    @Column(nullable = false)
    private String getClassMethodArgs;

    protected VisitorLog() {

    }

    public VisitorLog(String url, String httpMethod, String ip, String trueAddress, String classMethod, String getClassMethodArgs) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.ip = ip;
        this.trueAddress = trueAddress;
        this.classMethod = classMethod;
        this.getClassMethodArgs = getClassMethodArgs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public String getGetClassMethodArgs() {
        return getClassMethodArgs;
    }

    public void setGetClassMethodArgs(String getClassMethodArgs) {
        this.getClassMethodArgs = getClassMethodArgs;
    }
}
