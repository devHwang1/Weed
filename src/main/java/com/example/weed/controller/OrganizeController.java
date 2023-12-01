package com.example.weed.controller;

import com.example.weed.entity.Dept;
import com.example.weed.entity.Member;
import com.example.weed.repository.DeptRepository;
import com.example.weed.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice(annotations = Controller.class)
@AllArgsConstructor
public class OrganizeController {

    private final DeptRepository deptRepository;
    private final MemberRepository memberRepository;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        List<Dept> depts = deptRepository.findAll();
        model.addAttribute("depts", depts);

        long totalMembers = memberRepository.count();
        model.addAttribute("totalMembers",totalMembers);

    }
}