package com.whichard.spring.boot.blog.service;

import com.whichard.spring.boot.blog.domain.VisitorLog;

/**
 * Created by Whichard on 2018/4/15.
 */
public interface VisitorLogService {
    VisitorLog saveVistorLog(VisitorLog visitorLog);
}
