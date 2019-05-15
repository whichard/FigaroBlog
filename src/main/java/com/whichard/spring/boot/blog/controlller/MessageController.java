package com.whichard.spring.boot.blog.controlller;

import com.whichard.spring.boot.blog.domain.Message;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.service.MessageService;
import com.whichard.spring.boot.blog.service.UserService;
import com.whichard.spring.boot.blog.util.JsonUtil;
import com.whichard.spring.boot.blog.vo.ViewObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author wq
 * @date 2019/5/5
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @GetMapping("msg/del")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")
    public String delMessage(@Param("toId") int fromId) {
        User curr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(curr != null)
            messageService.deleteConversation(fromId, curr.getId().intValue());
        return "/msg/list";
    }

    @GetMapping("/msg/unread")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")
    public int getUnread() {
        User curr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return messageService.getTotalUnread(curr.getId().intValue());
    }

    @GetMapping("/msg/list")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")
    public String conversationDetail(Model model) {
        //try {
            User curr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int localUserId = curr.getId().intValue();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId);
            //去重
            //Comparator<Message> comparator = (Message o1, Message o2) -> (o1.getCreatedDate().compareTo(o2.getCreatedDate()));
            Set<Message> set = new TreeSet<Message>(new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    if(o1.getCreatedDate().before(o2.getCreatedDate()))
                        return 1;
                    else if(o1.getCreatedDate().after(o2.getCreatedDate())) return -1;
                    else return 0;
                }
            });
            for (Message msg : conversationList)
                set.add(msg);
            for (Message msg : set) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUserById((long)targetId);
                //System.out.println(msg);
                //制作跳转到发信人主页的头像链接
                vo.set("user", user);
                vo.set("conversationsCount", messageService.getConversationCount(msg.getConversationId()));
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        /*} catch (Exception e) {
            logger.error("获取站内信列表失败 " + e.getMessage());
        }*/
        return "message/letter";
    }

    @GetMapping("/msg/detail")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        User curr = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            List<Message> list = messageService.getConversionDetail(conversationId);
            List<Message> conversationList = new ArrayList<>();
            for(Message i : list) {
                if(i.getToId() == curr.getId())
                    conversationList.add(i);
            }
            List<ViewObject> messages = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message", msg);
                vo.set("content", msg.getContent());
                User user = userService.getUserById((long)msg.getFromId());
                vo.set("user", user);
                if(user != null) {
                    vo.set("headUrl", user.getHeadUrl());
                    vo.set("userName", user.getName());
                }
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }
        return "message/letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
            Message msg = new Message();
            msg.setContent(content);
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setCreatedDate(new Date());
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            messageService.addMessage(msg);
            return JsonUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
            return JsonUtil.getJSONString(1, "插入评论失败");
        }
    }
}
