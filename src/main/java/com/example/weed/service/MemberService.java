package com.example.weed.service;

import com.example.weed.domain.dept.Dept;
import com.example.weed.domain.dept.DeptRepository;
import com.example.weed.domain.members.Member;
import com.example.weed.domain.members.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeptRepository deptRepository;

    public List<Dept> getDeptListWithMembers() {
        return deptRepository.findAll();
    }


    public ArrayList<Member> ReadMember(){
        ArrayList<Member> memberArrayList = memberRepository.findAll();
        return memberArrayList;
    }

    public Member saveMember(Member member) {
        vaidateDuplicateMember(member);
        log.info("Before encoding: " + member.getPassword());

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodedPassword);

        log.info("After encoding: " + member.getPassword());
        return memberRepository.save(member);
    }

    private void vaidateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null && !findMember.getId().equals(member.getId())) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult =new HashMap<>();

        for(FieldError error :  errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName,error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email){
        boolean userEmailDuplicate = memberRepository.existsByName(email);
        return userEmailDuplicate;
    }







}
