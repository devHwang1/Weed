package com.example.weed.controller;

import com.example.weed.Util.W2001_JwtTokenUtil;
import com.example.weed.entity.Member;
import com.example.weed.entity.Working;
import com.example.weed.repository.W1001_MemberRepository;
import com.example.weed.repository.W2002_WorkingRepository;
import com.example.weed.service.W2001_QrJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://10.100.203.31:8099", allowCredentials = "true")
@RestController
@RequestMapping("/api/app/member")
public class W2001_MemberAppApiController {
    private final PasswordEncoder passwordEncoder;
    private final W1001_MemberRepository memberRepository;
    private final W2001_JwtTokenUtil jwtTokenUtil;
    private final W2001_QrJwtService jwtService;
    private final W2002_WorkingRepository w2002WorkingRepository;

    @Autowired
    public W2001_MemberAppApiController(PasswordEncoder passwordEncoder, W1001_MemberRepository memberRepository, W2001_JwtTokenUtil jwtTokenUtil, W2001_QrJwtService jwtService, W2002_WorkingRepository w2002WorkingRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtService = jwtService;
        this.w2002WorkingRepository = w2002WorkingRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member member) {
        String email = member.getEmail();
        String password = member.getPassword();

        Member foundMember = memberRepository.findByEmail(email).orElse(null);

        if (foundMember != null && passwordEncoder.matches(password, foundMember.getPassword())) {
            String authority = String.valueOf(foundMember.getAuthority());

            // 로그인 성공 시 Access 토큰 및 Refresh 토큰 생성
            String accessToken = jwtService.createAndReturnToken(email, Math.toIntExact(foundMember.getId()));
            String refreshToken = jwtTokenUtil.generateRefreshToken(email, Math.toIntExact(foundMember.getId()));

            // 토큰을 JSON 형식으로 응답
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("authority", authority);

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "로그인에 실패했습니다."));
        }
    }

    @PostMapping("/checkInOut")
    public ResponseEntity<Map<String, Object>> checkInOut(@RequestBody Map<String, String> requestData) {
        try {
            String token = requestData.get("token");

            // 토큰 해석
            String email = jwtTokenUtil.getEmailFromToken(token);
            Integer userId = jwtTokenUtil.getUserIdFromToken(token);

            // 해당하는 id와 email을 가진 유저 찾기
            Member foundMember = memberRepository.findById(userId.longValue()).orElse(null);

            if (foundMember != null && foundMember.getEmail().equals(email)) {
                // 출근 기록이 있는지 확인
                Working working = (Working) w2002WorkingRepository.findByMemberAndDate(foundMember, LocalDate.now()).orElse(null);

                if (working == null) {
                    // 출근 기록이 없으면 출근 처리
                    Working checkInWorking = new Working();
                    checkInWorking.setDate(LocalDate.now());
                    checkInWorking.setCheckInTime(LocalTime.now());
                    checkInWorking.setMember(foundMember);
                    w2002WorkingRepository.save(checkInWorking);

                    // 출근 성공 응답
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("checkIn", true);
                    return ResponseEntity.ok(response);
                } else {
                    // 출근 기록이 있으면 퇴근 처리
                    if (working.getCheckOutTime() == null) {
                        // 이미 출근한 경우
                        working.setCheckOutTime(LocalTime.now());
                        w2002WorkingRepository.save(working);

                        // 퇴근 성공 응답
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("checkIn", false);
                        return ResponseEntity.ok(response);
                    } else {
                        // 이미 퇴근한 경우
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("error", "이미 퇴근했습니다.");
                        return ResponseEntity.status(401).body(response);
                    }
                }
            } else {
                // 해당하는 유저를 찾지 못하면 실패 응답
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "해당 유저를 찾을 수 없습니다.");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            // 예외 처리
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "서버 오류");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 다른 필요한 API 엔드포인트들은 여기에 추가
}
