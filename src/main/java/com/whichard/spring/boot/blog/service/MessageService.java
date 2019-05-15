package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.Message;
import com.whichard.spring.boot.blog.repository.MessageDAO;
import com.whichard.spring.boot.blog.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wq
 * @date 2019/5/5
 */
@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;


    public int addMessage(Message message) {
        message.setConversationId();
        messageRepository.save(message);
        return 0; //may change
    }

    public void hasRead(Message message) {
        message.setHasRead(1);
        messageRepository.save(message);
    }

    public List<Message> getConversationList(int userId) {
        return messageRepository.getConversationList(userId);
    }

    public List<Message> getConversionDetail(String conversationId) {
        List<Message> list = messageRepository.findByConversationIdOrderByCreatedDateDesc(conversationId);
        for(Message msg : list) {
            hasRead(msg);
        }
        return list;
    }

    //全部未读消息
    public int getTotalUnread(int userId){
        return messageRepository.countByHasReadAndToId(0, userId);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageRepository.countByHasReadAndToIdAndConversationId(0, userId, conversationId);
    }

    public int getConversationCount(String conversationId) {
        return messageRepository.countByConversationId(conversationId);
    }

    public void deleteConversation(int fromId, int toId) {
        String conversationId;
        if(fromId < toId)
            conversationId = String.format("%d_%d", fromId, toId);
        conversationId = String.format("%d_%d", toId, fromId);
        messageRepository.deleteByToIdAndConversationId(toId, conversationId);
    }
}
