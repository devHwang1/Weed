package com.example.weed.controller;

import com.example.weed.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Log4j2
public class IndexController {
    private final MemberService memberService;
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

    @GetMapping("/sidebar")
    public String getSidebar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserDetails userDetails = memberService.loadUserByUsername(email);
        model.addAttribute("userSession", userDetails);

        return "sidebar";

    }
}