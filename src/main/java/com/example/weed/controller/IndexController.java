package com.example.weed.controller;

import com.example.weed.entity.Dept;
import com.example.weed.repository.DeptRepository;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@AllArgsConstructor
@Log4j2
public class IndexController {
    private final MemberService memberService;
    private final DeptRepository deptRepository;
    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user,Model model) {

        log.info("로그인세션확인@@@@@@@@@@@@@@@@@@@@{}",user);
//        List<Dept> depts = deptRepository.findAll();
//        model.addAttribute("depts", depts);
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
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}