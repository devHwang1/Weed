package com.example.weed.controller;

import com.example.weed.dto.W1001_MailDTO;
import com.example.weed.repository.W1003_DeptRepository;
import com.example.weed.service.W1001_MemberService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Controller
@AllArgsConstructor
@Log4j2
public class W1001_IndexController {
    private final W1001_MemberService w1001MemberService;
    private final W1003_DeptRepository w1003DeptRepository;
//    private final SendEmailService sendEmailService;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user,Model model) {

        log.info("로그인세션확인@@@@@@@@@@@@@@@@@@@@{}",user);
//        List<Dept> depts = deptRepository.findAll();
//        model.addAttribute("depts", depts);
        return "/layouts/W1003_qrcode";
    }

//    @GetMapping("/admin")
//    public String admin(){
//        return "W1009_admin";
//    }
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);


        return "W1001_login";
    }
    
    @GetMapping("/mypage")
    public String mypage() {
        return "W1008_mypage";
    }

    @GetMapping("/findPassword")
    public String findPassword() {
        return "W1001_findPassword";
    }

    // 이메일 보내기
    @Transactional
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("memberEmail") String memberEmail,Model model){
        W1001_MailDTO dto = w1001MemberService.createMailAndChangePassword(memberEmail);

        try {
            w1001MemberService.mailSend(dto);
            model.addAttribute("result", "success");
        } catch (Exception e) {
            log.error("메일 전송 중 오류 발생", e);
            model.addAttribute("result", "error");
        }

        return "W1001_login"; // sendEmailResult.html로 이동
    }


    @GetMapping("/register")
    public String register() {
        return "W1002_register";
    }

    @GetMapping("/sidebar")
    public String getSidebar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserDetails userDetails = w1001MemberService.loadUserByUsername(email);
        model.addAttribute("userSession", userDetails);

        return "/layouts/W1003_sidebar";

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