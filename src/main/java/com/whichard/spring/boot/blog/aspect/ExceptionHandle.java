package com.whichard.spring.boot.blog.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Whichard on 2018/3/21.
 */
@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ModelAndView handle(UsernameNotFoundException e) {
        System.out.println("捕获到了异常!");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login-error");
        modelAndView.addObject("loginError", true);
        modelAndView.addObject("errorMsg", "登陆失败，用户名或者密码错误！");
        return modelAndView;
    }
}
