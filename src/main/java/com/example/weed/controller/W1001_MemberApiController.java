package com.example.weed.controller;

import com.example.weed.dto.W1001_CustomDetails;
import com.example.weed.dto.W1001_MemberDTO;
import com.example.weed.dto.W1001_UserSessionDto;
import com.example.weed.entity.Member;
import com.example.weed.service.W1001_MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    //로그인에 응답하는 api 추가
    @GetMapping("/api/current")
    public ResponseEntity<W1001_UserSessionDto> getCurrentMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        W1001_CustomDetails customDetails = (W1001_CustomDetails) authentication.getPrincipal();
        Member loggedInMember = customDetails.getLoggedInMember();

        W1001_UserSessionDto userSessionDto = new W1001_UserSessionDto(loggedInMember);
        return ResponseEntity.ok(userSessionDto);
    }


    @PostMapping("/api/member")
    public ResponseEntity<String> save(@RequestBody @Valid Member.SaveRequest member, BindingResult bindingResult, Model model) {
        boolean isIdInUse = w1001MemberService.isEmailInUse(member.getEmail());

        if (isIdInUse) {
            return new ResponseEntity<>("이미 사용중인 이메일 입니다.", HttpStatus.BAD_REQUEST);
        }

        if (bindingResult.hasErrors()) {
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

