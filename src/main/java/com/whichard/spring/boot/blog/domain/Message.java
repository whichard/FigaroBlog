package com.whichard.spring.boot.blog.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author wq
 * @date 2019/5/5
 */
@Entity
public class Message {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private int id;

    @NotNull
    @Column(nullable = false) // 映射为字段，值不能为空
    private int fromId;

    @NotNull
    @Column(nullable = false) // 映射为字段，值不能为空
    private int toId;

    @NotEmpty(message = "content不能为空")
    @Size(min = 2, max = 500)
    @Column(nullable = false) // 映射为字段，值不能为空
    private String content;

    @Column(nullable = false) // 映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Date createdDate;

    @NotNull
    @Column(nullable = false) // 映射为字段，值不能为空
    private int hasRead;

    private String conversationId;

    public void setConversationId() {
        if(fromId < toId)
            this.conversationId = String.format("%d_%d", fromId, toId);
        this.conversationId = String.format("%d_%d", toId, fromId);
    }

    public String getConversationId() {
        if(fromId < toId)
            return String.format("%d_%d", fromId, toId);
        return String.format("%d_%d", toId, fromId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", hasRead=" + hasRead +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
