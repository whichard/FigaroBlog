package com.whichard.spring.boot.blog.controlller;

import com.whichard.spring.boot.blog.util.ApiResponse;
import com.whichard.spring.boot.blog.vo.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping("/autocomplete")
    @ResponseBody
    public ApiResponse autocomplete(@RequestParam(value = "prefix") String prefix) {

        if (prefix.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
        //ServiceResult<List<String>> result = this.searchService.suggest(prefix);
        List<String> result = new ArrayList<>();
        result.add("文强很棒");
        return ApiResponse.ofSuccess(result);
    }
}
