package com.example.weed.controller;

import com.example.weed.entity.Member;
import com.example.weed.service.W1001_MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class W1001_MemberApiController {
    private final W1001_MemberService w1001MemberService;

    public W1001_MemberApiController(W1001_MemberService w1001MemberService) {
        this.w1001MemberService = w1001MemberService;
    }

    @PostMapping("/api/member")
    public ResponseEntity<String> save(@RequestBody @Valid Member.SaveRequest member, BindingResult bindingResult, Model model) {
        // 이미 사용 중인 ID 확인
        boolean isIdInUse = w1001MemberService.isEmailInUse(member.getEmail());

        // 만약 사용 중이면 클라이언트에게 알려주고 회원가입을 막음
        if (isIdInUse) {
            return new ResponseEntity<>("이미 사용중인 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

        // 그 외의 경우에는 회원가입을 수행
        if (bindingResult.hasErrors()) {
            // 에러 세부 정보를 포함한 ResponseEntity를 구성하여 반환
            // 예를 들어, 오류 필드와 메시지를 포함한 JSON을 반환할 수 있습니다
            StringBuilder errorMessage = new StringBuilder("입력값이 올바르지 않습니다:\n");
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        w1001MemberService.save(member);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }

    @GetMapping("/api/member/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email) {
        boolean isEmailInUse = w1001MemberService.isEmailInUse(email);

        return new ResponseEntity<>(isEmailInUse ? "이미 사용중인 ID 입니다." : "사용 가능한 ID 입니다.", HttpStatus.OK);
    }
}

