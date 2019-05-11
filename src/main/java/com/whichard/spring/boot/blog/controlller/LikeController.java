package com.whichard.spring.boot.blog.controlller;

import com.whichard.spring.boot.blog.async.EventModel;
import com.whichard.spring.boot.blog.async.EventProducer;
import com.whichard.spring.boot.blog.async.EventType;
import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.service.BlogService;
import com.whichard.spring.boot.blog.service.LikeService;
import com.whichard.spring.boot.blog.service.UserService;
import com.whichard.spring.boot.blog.service.VoteService;
import com.whichard.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.whichard.spring.boot.blog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

/**
 * @author wq
 * @date 2019/4/18
 */
@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @Autowired
    EventProducer eventProducer;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")
    public ResponseEntity<Response> likeVote(Long blogId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            long like = likeService.like(user.getId(), 0,blogId);
            blogService.createVote(blogId);

            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(user.getId().intValue()).setEntityId(blogId.intValue())
                    );
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_VISTOR')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> delete(@PathVariable("id")Long blogId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long id = principal.getId();
        try {
            likeService.cancelLike(id, 0, blogId);
            //!!下面的删除出错，需要看@ManyToMany
            /*voteService.removeVote(id);
            blogService.removeVote(blogId, id);*/

        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "取消点赞成功", null));
    }
}
