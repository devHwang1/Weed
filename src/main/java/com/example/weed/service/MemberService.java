package com.example.weed.service;

import com.example.weed.domain.dept.Dept;
import com.example.weed.domain.dept.DeptRepository;
import com.example.weed.domain.members.Member;
import com.example.weed.domain.members.MemberRepository;
import javassist.bytecode.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

@Slf4j
@Transactional
@Service
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeptRepository deptRepository;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, DeptRepository deptRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.deptRepository = deptRepository;
    }



    public boolean login(Member member) {
        log.info("Entering login method");

        Optional<Member> optionalUser = memberRepository.findByEmail(member.getEmail());

        return optionalUser.map(findUser -> {
            log.info("User found in the database");

            if (findUser.getPassword() == null) {
                log.info("User password is null");
                return false;
            }

            log.info("User password: {}", findUser.getPassword());

            // 입력된 비밀번호를 저장된 암호와 비교
            boolean passwordMatches = passwordEncoder.matches(member.getPassword(), findUser.getPassword());
            log.info("Password matches: {}", passwordMatches);

            return passwordMatches;
        }).orElseGet(() -> {
            log.info("User not found in the database");
            return false;
        });
    }






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
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(existingMember -> {
                    log.info("Duplicate member found with email: {}", member.getEmail());
                    try {
                        throw new DuplicateMemberException("Duplicate member found with email: " + member.getEmail());
                    } catch (DuplicateMemberException e) {
                        throw new RuntimeException(e);
                    }
                });
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

    //조직도에 전체인원 뽑아오기
    public int getTotalMembers() {
        return (int) memberRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);

        return toUserDetails(member);
    }

    private UserDetails toUserDetails(Optional<Member> member) {
        if (member.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username(String.valueOf(member.get().getId()))
                .password(member.get().getPassword())
                .authorities(new SimpleGrantedAuthority(member.get().getRole().getName()))
                .build();
    }




}

