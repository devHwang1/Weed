package com.example.weed.controller;

import com.example.weed.Util.W2001_JwtTokenUtil;
import com.example.weed.entity.Member;
import com.example.weed.repository.MemberRepository;
import com.example.weed.service.W2001_QrJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://10.100.203.31:8099", allowCredentials = "true")
@RestController
@RequestMapping("/api/app/member")
public class W2001_MemberAppApiController {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final W2001_JwtTokenUtil jwtTokenUtil;
    private final W2001_QrJwtService jwtService;

    @Autowired
    public W2001_MemberAppApiController(PasswordEncoder passwordEncoder, MemberRepository memberRepository, W2001_JwtTokenUtil jwtTokenUtil, W2001_QrJwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member member) {
        String email = member.getEmail();
        String password = member.getPassword();

        Member foundMember = memberRepository.findByEmail(email).orElse(null);

        if (foundMember != null && passwordEncoder.matches(password, foundMember.getPassword())) {
            // 로그인 성공 시 Access 토큰 및 Refresh 토큰 생성
            String accessToken = jwtService.createAndReturnToken(email, Math.toIntExact(foundMember.getId()));
            String refreshToken = jwtTokenUtil.generateRefreshToken(email, Math.toIntExact(foundMember.getId()));

            // 토큰을 JSON 형식으로 응답
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "로그인에 실패했습니다."));
        }
    }

//    @PostMapping("/working")

    // 다른 필요한 API 엔드포인트들은 여기에 추가
}
