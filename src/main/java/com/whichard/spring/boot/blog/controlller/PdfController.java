package com.whichard.spring.boot.blog.controlller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PDF服务控制器
 *
 * @author <a href="http://www.whichard.cn">Whichard</a>
 * @since 1.0.0 2018年5月16日
 */

@Controller
@RequestMapping(value = "/pdf")
public class PdfController {

    @GetMapping(value = "/resume")
    public String resumeService() {
        return "/pdf/resume";
    }
}
