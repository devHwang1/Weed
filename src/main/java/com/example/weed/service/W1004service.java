package com.example.weed.service;

import com.example.weed.entity.Member;
import com.example.weed.entity.W1004Entity;
import com.example.weed.repository.W1004Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class W1004service {
    @Autowired
    private W1001_MemberService memberService;

    @Autowired
    private W1004Repository w1004Repository;

    @Transactional
    public void saveSchedule(W1004Entity w1004Entity){
        Member loggedInMember = memberService.getLoggedInMember();      //로그인정보 가져오기

        if (loggedInMember != null) {
            //스케줄 엔터티에 멤버 설정
            w1004Entity.setMember(loggedInMember);

            //스케줄 저장
            w1004Repository.save(w1004Entity);
        } else {
            // 로그인되지 않은 경우에 대한 처리
            throw new IllegalStateException("로그인된 멤버가 없습니다.");
        }
    }
}
