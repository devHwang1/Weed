package com.example.weed.controller;

import com.example.weed.entity.Member;
import com.example.weed.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/members")
public class W2001_MemberAppApiController {

    private final MemberRepository memberRepository;

    @Autowired
    public W2001_MemberAppApiController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Member member) {
        String email = member.getEmail();
        String password = member.getPassword();

        Member foundMember = memberRepository.findByEmail(email)
                .orElse(null);

        if (foundMember != null && foundMember.getPassword().equals(password)) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(401).body("로그인 실패");
        }
    }

    // 다른 필요한 API 엔드포인트들은 여기에 추가
}
