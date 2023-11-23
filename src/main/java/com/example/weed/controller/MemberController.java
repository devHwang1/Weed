package com.example.weed.controller;

import com.example.weed.domain.dept.Dept;
import com.example.weed.domain.members.CheckUsernameValidator;
import com.example.weed.domain.members.Member;
import com.example.weed.dto.MemberFormDto;
import com.example.weed.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final CheckUsernameValidator checkUsernameValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUsernameValidator);
    }

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());

        List<Dept> deptList = memberService.getDeptListWithMembers();
        model.addAttribute("deptList", deptList);
        return "/register";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model , Errors errors){

        if(errors.hasErrors()){
            model.addAttribute("memberFormDto",memberFormDto);

            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/register";
        }
        boolean emailDuplicate = memberService.checkEmailDuplication(memberFormDto.getEmail());
        if (emailDuplicate) {
            model.addAttribute("valid_email", "중복된 이메일입니다.");
            return "/register";
        }

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);
        return "redirect:/main";

    }


    @GetMapping("/new/{email}/exists")
    public ResponseEntity<Boolean> checkUserEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(memberService.checkEmailDuplication(email));
    }

}
