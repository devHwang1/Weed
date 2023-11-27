package com.example.weed.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class IndexController {

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user) {
        log.info("로그인세션확인@@@@@@@@@@@@@@@@@@@@{}",user);
        return "layouts/qrcode";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }
}