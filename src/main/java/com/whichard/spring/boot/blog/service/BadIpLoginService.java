package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.User;
import com.whichard.spring.boot.blog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class BadIpLoginService implements ApplicationListener<AuthenticationSuccessEvent> {

    String currentAddress = null;

    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                e.getAuthentication().getDetails();

        String ip = auth.getRemoteAddress();
        System.out.println(ip);
        String addr = IpUtil.getAddressByIP(ip);
        //System.out.println(addr);
        currentAddress = addr;

    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}