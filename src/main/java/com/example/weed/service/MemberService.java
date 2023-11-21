package com.example.weed.service;

import com.example.weed.domain.members.Member;
import com.example.weed.domain.members.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        vaidateDuplicateMember(member);

        return memberRepository.save(member);
    }

    private void vaidateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


}
