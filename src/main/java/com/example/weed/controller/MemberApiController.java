package com.example.weed.controller;

import com.example.weed.entity.Member;
import com.example.weed.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/api/member")
    public void save(@RequestBody Member.SaveRequest member) {
        memberService.save(member);
    }
}

