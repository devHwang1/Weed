package com.example.weed.controller;

import com.example.weed.dto.CustomDetails;
import com.example.weed.dto.W1004DTO;
import com.example.weed.entity.Member;
import com.example.weed.entity.W1004Entity;
import com.example.weed.repository.W1004Repository;
import com.example.weed.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class W1004Controller {

    @Autowired
    private W1004Repository w1004Repository;


    @Autowired
    private MemberService memberService;

    @GetMapping("/calendar")
    public String calendar() {
        return "W1004";
    }


    @PostMapping("/saveW1004")
    @ResponseBody
    public String saveEvent(@RequestBody W1004DTO w1004dto) {

        // EventData를 Entity로 변환
        W1004Entity entity = new W1004Entity();
        entity.setScheduleId(w1004dto.getEventId());
        entity.setScheduleTitle(w1004dto.getTitle());
        entity.setScheduleStart(w1004dto.getStartDate());
        entity.setScheduleEnd(w1004dto.getEndDate());
        entity.setScheduleColor(w1004dto.getColor());
        entity.setScheduleContent(w1004dto.getContent());

        // 현재 로그인한 멤버의 ID 가져오기
        Long loggedInMemberId = getLoggedInMemberId();

        // 해당 ID로 Member 엔터티 가져오기
        Member loggedInMember = memberService.findById(loggedInMemberId);

        // Member 엔터티를 W1004Entity에 설정
        entity.setMember(loggedInMember);

        //db저장
        w1004Repository.save(entity);

        return "success";
    }

    // 현재 로그인한 멤버의 ID를 가져오는 메서드
    private Long getLoggedInMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomDetails) {
                CustomDetails customDetails = (CustomDetails) principal;
                Member loggedInMember = customDetails.getLoggedInMember();
                if (loggedInMember != null) {
                    return loggedInMember.getId();
                }
            }
        }
        return null;
    }


}
