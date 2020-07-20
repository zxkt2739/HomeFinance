package com.example.module1.controller;

import com.example.core.common.R;
import com.example.core.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping
    public Object list(HttpServletRequest request) {
        return R.success();
    }

    @GetMapping("/dd")
    public Object dd(HttpServletRequest request) {

        return R.success();
    }
}
