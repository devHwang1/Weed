package com.example.weed.controller;

import com.example.weed.dto.MailDTO;
import com.example.weed.repository.DeptRepository;
import com.example.weed.service.MemberService;
//import com.example.weed.service.SendEmailService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Controller
@AllArgsConstructor
@Log4j2
public class IndexController {
    private final MemberService memberService;
    private final DeptRepository deptRepository;
//    private final SendEmailService sendEmailService;

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
    
    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }


    @GetMapping("/findPassword")
    public String findPassword() {
        return "findPassword";
    }

    // 이메일 보내기
    @Transactional
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("memberEmail") String memberEmail,Model model){
        MailDTO dto = memberService.createMailAndChangePassword(memberEmail);

        try {
            memberService.mailSend(dto);
            model.addAttribute("result", "success");
        } catch (Exception e) {
            log.error("메일 전송 중 오류 발생", e);
            model.addAttribute("result", "error");
        }

        return "login"; // sendEmailResult.html로 이동
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