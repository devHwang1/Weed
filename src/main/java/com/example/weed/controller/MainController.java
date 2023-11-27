package com.example.weed.controller;

import com.example.weed.domain.dept.Dept;
import com.example.weed.domain.members.Member;
import com.example.weed.dto.MemberFormDto;
import com.example.weed.service.MemberService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping("/main")
    public String main(Model model) {

        List<Dept> deptList = memberService.getDeptListWithMembers();
        model.addAttribute("deptList", deptList);

        int totalMembers = memberService.getTotalMembers();
        model.addAttribute("totalMembers", totalMembers);
        return "/layouts/main";
    }




}
