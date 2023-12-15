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


    //현재로그인한 멤버정보 가져오기
    @GetMapping("/api/current")
    public ResponseEntity<W1001_UserSessionDto> getCurrentMember() {
        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 있는지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 인증 정보에서 사용자 정보 추출
        W1001_CustomDetails customDetails = (W1001_CustomDetails) authentication.getPrincipal();
        Member loggedInMember = customDetails.getLoggedInMember();

        // 사용자 정보가 있는지 확인
        if (loggedInMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 사용자 정보가 정상적으로 추출되는지 확인
        System.out.println("CustomDetails: " + customDetails);
        System.out.println("LoggedInMember: " + loggedInMember);

        // 사용자 정보를 DTO에 매핑하여 반환
        W1001_UserSessionDto userSessionDto = new W1001_UserSessionDto(loggedInMember);

        // 멤버 아이디 추가
        userSessionDto.setId(loggedInMember.getId());
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

    @PutMapping("/api/updateMemberAuthority/{memberId}")
    public ResponseEntity<String> updateMemberAuthority(@PathVariable Long memberId, @RequestParam String authority){
        try {
            w1001MemberService.updateMemberAuthority(memberId,authority);
            return ResponseEntity.ok("권한 변경 완료!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating 권한");
        }
    }

    @PutMapping("/api/updateMemberDept/{memberId}")
    public ResponseEntity<String> updateDept(@PathVariable Long memberId, @RequestParam Long deptId){
        try {
            w1001MemberService.updateDept(memberId,deptId);
            return (ResponseEntity<String>) ResponseEntity.ok("부서 변경 완료!");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("부서변경 에러");
        }
    }


}

